// Import shared libraries
@Library('my-shared-library') _

pipeline {
    // Define where pipeline runs
    agent {
        label 'linux-docker'
    }
    
    // Pipeline-wide options
    options {
        timeout(time: 2, unit: 'HOURS')
        timestamps()
        retry(3)
        skipDefaultCheckout()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        parallelsAlwaysFailFast()
    }
    
    // Auto-install tools
    tools {
        maven 'Maven-3.8.1'
        jdk 'JDK-11'
        nodejs 'NodeJS-16'
        gradle 'Gradle-7.0'
    }
    
    // Define build parameters
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to build')
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: 'Target environment')
        booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'Skip test execution')
        text(name: 'DEPLOYMENT_NOTES', defaultValue: '', description: 'Deployment notes')
        password(name: 'API_SECRET', defaultValue: '', description: 'API Secret Key')
    }
    
    // Define automated triggers
    triggers {
        cron('H 2 * * 1-5')  // Weekdays at 2 AM
        pollSCM('H/15 * * * *')  // Poll SCM every 15 minutes
        upstream(upstreamProjects: 'upstream-job', threshold: hudson.model.Result.SUCCESS)
    }
    
    // Environment variables
    environment {
        APP_NAME = 'my-application'
        BUILD_VERSION = "${BUILD_NUMBER}"
        DOCKER_REGISTRY = 'my-registry.com'
        PATH = "${PATH}:/usr/local/bin"
        DEBUG_MODE = "${params.ENVIRONMENT == 'dev' ? 'true' : 'false'}"
    }
    
    // Main pipeline stages
    stages {
        
        // Checkout and setup stage
        stage('Checkout & Setup') {
            agent {
                docker {
                    image 'alpine/git:latest'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            
            when {
                anyOf {
                    branch 'main'
                    branch 'develop'
                    changeRequest()
                }
            }
            
            steps {
                script {
                    // Scripted pipeline within declarative
                    def gitInfo = checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH_NAME}"]],
                        userRemoteConfigs: [[url: 'https://github.com/myorg/myrepo.git']]
                    ])
                    
                    env.GIT_COMMIT_SHORT = gitInfo.GIT_COMMIT[0..7]
                    
                    // File operations
                    writeFile file: 'build-info.txt', text: """
                        Build Number: ${BUILD_NUMBER}
                        Git Commit: ${env.GIT_COMMIT}
                        Branch: ${env.BRANCH_NAME}
                        Build Date: ${new Date()}
                        Environment: ${params.ENVIRONMENT}
                    """
                    
                    if (fileExists('package.json')) {
                        echo "Node.js project detected"
                        env.PROJECT_TYPE = 'nodejs'
                    } else if (fileExists('pom.xml')) {
                        echo "Maven project detected"
                        env.PROJECT_TYPE = 'maven'
                    }
                    
                    echo "Current directory: ${pwd()}"
                    echo "Workspace: ${env.WORKSPACE}"
                }
                
                // Archive build info
                archiveArtifacts artifacts: 'build-info.txt', fingerprint: true
            }
        }
        
        // Build stage with parallel execution
        stage('Build') {
            parallel {
                stage('Backend Build') {
                    agent {
                        docker {
                            image 'maven:3.8.1-openjdk-11'
                            args '-v /root/.m2:/root/.m2'
                        }
                    }
                    
                    steps {
                        timeout(time: 30, unit: 'MINUTES') {
                            sh 'mvn clean compile -DskipTests'
                            sh 'mvn package -DskipTests'
                        }
                        
                        dir('target') {
                            archiveArtifacts artifacts: '*.jar', allowEmptyArchive: true
                        }
                    }
                    
                    post {
                        always {
                            echo "Backend build completed"
                        }
                        failure {
                            error "Backend build failed!"
                        }
                    }
                }
                
                stage('Frontend Build') {
                    agent {
                        docker {
                            image 'node:16-alpine'
                        }
                    }
                    
                    steps {
                        timeout(time: 20, unit: 'MINUTES') {
                            sh 'npm ci'
                            sh 'npm run build'
                        }
                        
                        dir('dist') {
                            archiveArtifacts artifacts: '**/*', allowEmptyArchive: true
                        }
                    }
                }
                
                stage('Database Migration Check') {
                    when {
                        expression { params.ENVIRONMENT != 'dev' }
                    }
                    
                    steps {
                        script {
                            try {
                                sh 'flyway info'
                                sh 'flyway validate'
                            } catch (Exception e) {
                                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                                    error "Database migration validation failed: ${e.getMessage()}"
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Testing stage with conditions
        stage('Testing') {
            when {
                not { params.SKIP_TESTS }
            }
            
            parallel {
                stage('Unit Tests') {
                    steps {
                        retry(2) {
                            sh 'mvn test'
                        }
                        
                        // Publish test results
                        junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    }
                    
                    post {
                        always {
                            publishHTML([
                                allowMissing: false,
                                alwaysLinkToLastBuild: true,
                                keepAll: true,
                                reportDir: 'target/site/jacoco',
                                reportFiles: 'index.html',
                                reportName: 'Code Coverage Report'
                            ])
                        }
                    }
                }
                
                stage('Integration Tests') {
                    steps {
                        script {
                            def testCommands = [
                                'mvn verify -Dtest.profile=integration',
                                'npm run test:integration'
                            ]
                            
                            for (cmd in testCommands) {
                                timeout(time: 15, unit: 'MINUTES') {
                                    sh cmd
                                }
                            }
                        }
                    }
                }
                
                stage('Security Scan') {
                    steps {
                        withCredentials([
                            string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN'),
                            usernamePassword(credentialsId: 'registry-creds', usernameVariable: 'REGISTRY_USER', passwordVariable: 'REGISTRY_PASS')
                        ]) {
                            sh '''
                                sonar-scanner \
                                  -Dsonar.projectKey=${APP_NAME} \
                                  -Dsonar.sources=. \
                                  -Dsonar.host.url=http://sonarqube:9000 \
                                  -Dsonar.login=${SONAR_TOKEN}
                            '''
                        }
                    }
                }
            }
        }
        
        // Wait for approval for production
        stage('Manual Approval') {
            when {
                expression { params.ENVIRONMENT == 'prod' }
            }
            
            steps {
                timeout(time: 24, unit: 'HOURS') {
                    input message: 'Deploy to Production?', 
                          ok: 'Deploy',
                          submitterParameter: 'APPROVER',
                          parameters: [
                              choice(name: 'DEPLOYMENT_STRATEGY', choices: ['blue-green', 'rolling', 'canary']),
                              string(name: 'ROLLBACK_VERSION', defaultValue: '', description: 'Version to rollback to if needed')
                          ]
                }
            }
        }
        
        // Matrix build for multiple environments
        stage('Deploy') {
            matrix {
                axes {
                    axis {
                        name 'DEPLOY_ENV'
                        values 'staging', 'prod-east', 'prod-west'
                    }
                    axis {
                        name 'DEPLOY_STRATEGY'
                        values 'rolling', 'blue-green'
                    }
                }
                excludes {
                    exclude {
                        axis {
                            name 'DEPLOY_ENV'
                            values 'staging'
                        }
                        axis {
                            name 'DEPLOY_STRATEGY'
                            values 'blue-green'
                        }
                    }
                }
                
                stages {
                    stage('Deploy to Environment') {
                        steps {
                            script {
                                echo "Deploying to ${DEPLOY_ENV} using ${DEPLOY_STRATEGY} strategy"
                                
                                // Wait for external condition
                                waitUntil(initialRecurrencePeriod: 15000) {
                                    def result = sh(
                                        script: "curl -f http://${DEPLOY_ENV}-health-check/status",
                                        returnStatus: true
                                    )
                                    return result == 0
                                }
                                
                                // Deployment commands
                                sh """
                                    docker build -t ${DOCKER_REGISTRY}/${APP_NAME}:${BUILD_VERSION} .
                                    docker push ${DOCKER_REGISTRY}/${APP_NAME}:${BUILD_VERSION}
                                    
                                    helm upgrade --install ${APP_NAME}-${DEPLOY_ENV} ./helm-chart \
                                      --set image.tag=${BUILD_VERSION} \
                                      --set environment=${DEPLOY_ENV} \
                                      --set strategy=${DEPLOY_STRATEGY}
                                """
                            }
                        }
                    }
                }
            }
        }
        
        // Smoke tests after deployment
        stage('Smoke Tests') {
            steps {
                script {
                    sleep time: 30, unit: 'SECONDS' // Wait for deployment to stabilize
                    
                    def endpoints = [
                        "${params.ENVIRONMENT}-api.example.com/health",
                        "${params.ENVIRONMENT}-api.example.com/metrics"
                    ]
                    
                    endpoints.each { endpoint ->
                        retry(3) {
                            sh "curl -f http://${endpoint} || exit 1"
                        }
                    }
                }
                
                echo "All smoke tests passed!"
            }
        }
        
        // Notification stage
        stage('Notification') {
            steps {
                script {
                    def deploymentNotes = params.DEPLOYMENT_NOTES ?: 'No deployment notes provided'
                    def buildContent = readFile('build-info.txt')
                    
                    // Send notifications
                    emailext(
                        to: 'team@company.com, ops@company.com',
                        subject: "✅ Deployment Successful - ${APP_NAME} v${BUILD_VERSION}",
                        body: """
                            <h2>Deployment Successful</h2>
                            <p><strong>Application:</strong> ${APP_NAME}</p>
                            <p><strong>Version:</strong> ${BUILD_VERSION}</p>
                            <p><strong>Environment:</strong> ${params.ENVIRONMENT}</p>
                            <p><strong>Approver:</strong> ${env.APPROVER ?: 'N/A'}</p>
                            
                            <h3>Build Information:</h3>
                            <pre>${buildContent}</pre>
                            
                            <h3>Deployment Notes:</h3>
                            <p>${deploymentNotes}</p>
                            
                            <p><a href="${BUILD_URL}">View Build Details</a></p>
                        """,
                        mimeType: 'text/html',
                        attachmentsPattern: 'build-info.txt'
                    )
                }
            }
        }
        
        // Cleanup stage
        stage('Cleanup') {
            steps {
                script {
                    // Clean up old Docker images
                    sh '''
                        docker images ${DOCKER_REGISTRY}/${APP_NAME} --format "table {{.Repository}}\\t{{.Tag}}\\t{{.CreatedAt}}" | \
                        tail -n +2 | sort -k3 -r | tail -n +6 | \
                        awk '{print $1":"$2}' | xargs -r docker rmi || true
                    '''
                    
                    // Clean workspace selectively
                    dir('temp') {
                        deleteDir()
                    }
                }
                
                bat 'echo "Cleanup completed on Windows agent"' // Windows command example
                powershell 'Write-Host "PowerShell cleanup completed"' // PowerShell example
            }
        }
    }
    
    // Post-build actions
    post {
        always {
            echo "Pipeline execution completed for ${APP_NAME} v${BUILD_VERSION}"
            echo "Build result: ${currentBuild.currentResult}"
            echo "Build duration: ${currentBuild.durationString}"
            
            // Always archive important artifacts
            archiveArtifacts artifacts: '**/target/*.jar, **/dist/**, build-info.txt', 
                            allowEmptyArchive: true,
                            fingerprint: true
            
            // Clean workspace
            cleanWs(
                cleanWhenAborted: true,
                cleanWhenFailure: false,
                cleanWhenNotBuilt: false,
                cleanWhenSuccess: true,
                cleanWhenUnstable: false,
                deleteDirs: true
            )
        }
        
        success {
            echo "🎉 Pipeline succeeded!"
            
            script {
                currentBuild.description = "✅ Deployed v${BUILD_VERSION} to ${params.ENVIRONMENT}"
                
                // Trigger downstream jobs
                build job: 'post-deployment-tests',
                      parameters: [
                          string(name: 'VERSION', value: env.BUILD_VERSION),
                          string(name: 'ENVIRONMENT', value: params.ENVIRONMENT)
                      ],
                      wait: false
            }
            
            // Success notification
            slackSend(
                channel: '#deployments',
                color: 'good',
                message: "✅ ${APP_NAME} v${BUILD_VERSION} successfully deployed to ${params.ENVIRONMENT}"
            )
        }
        
        failure {
            echo "❌ Pipeline failed!"
            
            script {
                currentBuild.description = "❌ Failed at ${env.STAGE_NAME}"
                
                def failureReason = currentBuild.getBuildCauses()[0]?.shortDescription ?: 'Unknown failure'
                
                emailext(
                    to: 'team@company.com, oncall@company.com',
                    subject: "🚨 URGENT: Deployment Failed - ${APP_NAME}",
                    body: """
                        <h2 style="color: red;">Deployment Failed</h2>
                        <p><strong>Application:</strong> ${APP_NAME}</p>
                        <p><strong>Version:</strong> ${BUILD_VERSION}</p>
                        <p><strong>Environment:</strong> ${params.ENVIRONMENT}</p>
                        <p><strong>Failed Stage:</strong> ${env.STAGE_NAME}</p>
                        <p><strong>Failure Reason:</strong> ${failureReason}</p>
                        
                        <p><a href="${BUILD_URL}console">View Console Output</a></p>
                        
                        <h3>Recommended Actions:</h3>
                        <ul>
                            <li>Check the console output for detailed error messages</li>
                            <li>Verify all dependencies are available</li>
                            <li>Check if rollback is needed for production deployments</li>
                        </ul>
                    """,
                    mimeType: 'text/html'
                )
            }
        }
        
        unstable {
            echo "⚠️ Pipeline completed with warnings"
            
            slackSend(
                channel: '#deployments',
                color: 'warning',
                message: "⚠️ ${APP_NAME} v${BUILD_VERSION} deployed with warnings to ${params.ENVIRONMENT}"
            )
        }
        
        aborted {
            echo "🛑 Pipeline was aborted"
            
            emailext(
                to: "${env.APPROVER}@company.com",
                subject: "Pipeline Aborted - ${APP_NAME}",
                body: "The pipeline for ${APP_NAME} was aborted during execution."
            )
        }
        
        changed {
            echo "📊 Build result changed from previous build"
            
            script {
                def previousResult = currentBuild.previousBuild?.result ?: 'UNKNOWN'
                def currentResult = currentBuild.currentResult
                
                slackSend(
                    channel: '#build-status',
                    message: "📊 ${APP_NAME} build status changed: ${previousResult} → ${currentResult}"
                )
            }
        }
        
        cleanup {
            echo "🧹 Final cleanup operations"
            
            script {
                // Cleanup any remaining resources
                sh 'docker system prune -f --volumes || true'
                
                // Log final build metrics
                def buildMetrics = [
                    duration: currentBuild.duration,
                    result: currentBuild.currentResult,
                    testCount: currentBuild.testResultAction?.totalCount ?: 0,
                    failedTests: currentBuild.testResultAction?.failCount ?: 0
                ]
                
                writeFile file: 'build-metrics.json', text: groovy.json.JsonBuilder(buildMetrics).toPrettyString()
                archiveArtifacts artifacts: 'build-metrics.json'
            }
        }
    }
}