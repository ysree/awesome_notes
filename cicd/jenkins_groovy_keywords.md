# Jenkins Pipeline Keywords Reference

## 1. Pipeline Types & Root Keywords

| Keyword | Description | Usage |
|---------|-------------|-------|
| `pipeline` | Root block for modern **Declarative Pipeline** syntax (recommended) | `pipeline { ... }` |
| `node` | Root block for **Scripted Pipeline** - allocates executor and workspace | `node('label') { ... }` |

## 2. Declarative Pipeline Sections

### Core Structure
| Keyword | Description |
|---------|-------------|
| `agent` | Defines where pipeline/stage runs (`any`, `none`, `label`, `docker`) |
| `stages` | Container for multiple stage definitions |
| `stage` | Major unit of work (e.g., "Build", "Test", "Deploy") |
| `steps` | Contains actual tasks/commands within a stage |

### Configuration Sections
| Keyword | Description |
|---------|-------------|
| `environment` | Define key-value environment variables |
| `options` | Pipeline-wide options (timeout, timestamps, retry) |
| `parameters` | Define input parameters (string, boolean, choice, text, password) |
| `triggers` | Automated triggers (cron, pollSCM, upstream) |
| `tools` | Auto-install tools (maven, gradle, nodejs, jdk) |
| `libraries` | Load shared libraries |

### Conditional & Flow Control
| Keyword | Description |
|---------|-------------|
| `when` | Conditional execution of stages |
| `input` | Pause for human input/approval |
| `post` | Actions to run based on build result |

## 3. Post-Build Action Keywords

| Keyword | Description |
|---------|-------------|
| `always` | Run regardless of build result |
| `success` | Run only on successful builds |
| `failure` | Run only on failed builds |
| `unstable` | Run when build is unstable |
| `aborted` | Run if build was aborted |
| `changed` | Run when build result changes from previous |
| `cleanup` | Run after all other post conditions |

## 4. Common Step Commands

### Execution Commands
| Keyword | Description | Platform |
|---------|-------------|----------|
| `sh` | Execute shell commands | Unix/Linux/Mac |
| `bat` | Execute batch commands | Windows |
| `powershell` | Execute PowerShell script | Windows |
| `script` | Switch to scripted syntax within declarative pipeline | All |

### Output & Messaging
| Keyword | Description |
|---------|-------------|
| `echo` | Print message to console |
| `error` | Fail build with custom message |
| `catchError` | Catch error but continue pipeline |

### File Operations
| Keyword | Description |
|---------|-------------|
| `checkout` | Check out source code from SCM |
| `readFile` | Read file content |
| `writeFile` | Write content to file |
| `deleteDir` | Delete workspace directory |
| `dir` | Change working directory for block |
| `fileExists` | Check if file exists |
| `pwd` | Get current directory |

### Artifacts & Reports
| Keyword | Description |
|---------|-------------|
| `archiveArtifacts` | Archive build artifacts |
| `publishHTML` | Publish HTML reports |
| `junit` | Publish test results |

### Build Integration
| Keyword | Description |
|---------|-------------|
| `build` | Trigger other Jenkins jobs |
| `emailext` | Send extended email notifications |

## 5. Flow Control & Advanced Keywords

### Parallel & Timing
| Keyword | Description |
|---------|-------------|
| `parallel` | Execute stages/steps concurrently |
| `matrix` | Matrix builds with different combinations |
| `sleep` | Pause execution for specified time |
| `timeout` | Set time limits for execution |
| `retry` | Retry steps N times on failure |
| `waitUntil` | Wait until condition becomes true |

### Security & Credentials
| Keyword | Description |
|---------|-------------|
| `withCredentials` | Bind credentials to variables (masked in logs) |

### Utility
| Keyword | Description |
|---------|-------------|
| `wrap` | Wrap steps with general-purpose utilities |

## 6. Scripted Pipeline Keywords

### Control Structures
| Keyword | Description |
|---------|-------------|
| `if` / `else` | Conditional execution |
| `try` / `catch` / `finally` | Exception handling |
| `for` | Loop iteration |
| `while` | While loop |
| `def` | Define variables in Groovy |
| `return` | Exit with return value |

## 7. Built-in Variables & Objects

### Global Variables
| Variable | Description |
|----------|-------------|
| `env` | Environment variables |
| `params` | Build parameters |
| `currentBuild` | Current build information object |

### Common Environment Variables
| Variable | Description |
|----------|-------------|
| `BUILD_NUMBER` | Current build number |
| `JOB_NAME` | Jenkins job name |
| `WORKSPACE` | Workspace directory path |
| `BUILD_URL` | URL to current build |
| `GIT_BRANCH` | Git branch name |
| `GIT_COMMIT` | Git commit hash |

## 8. Parameter Types

| Type | Description |
|------|-------------|
| `string` | Single-line text parameter |
| `text` | Multi-line text parameter |
| `booleanParam` | True/false checkbox |
| `choice` | Dropdown selection |
| `password` | Masked password input |

## 9. Agent Types

| Type | Description |
|------|-------------|
| `any` | Run on any available agent |
| `none` | No global agent (stages define their own) |
| `label 'name'` | Run on agent with specific label |
| `docker { ... }` | Run inside Docker container |

## Example Usage Patterns

```groovy
pipeline {
    agent any
    
    environment {
        APP_NAME = 'my-app'
    }
    
    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'])
        booleanParam(name: 'SKIP_TESTS', defaultValue: false)
    }
    
    stages {
        stage('Build') {
            steps {
                sh 'echo "Building ${APP_NAME}"'
            }
        }
        
        stage('Test') {
            when {
                not { params.SKIP_TESTS }
            }
            parallel {
                stage('Unit Tests') {
                    steps {
                        sh 'npm test'
                    }
                }
                stage('Integration Tests') {
                    steps {
                        sh 'npm run test:integration'
                    }
                }
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: '**/*.log'
        }
        failure {
            emailext to: 'team@company.com',
                     subject: 'Build Failed'
        }
    }
}
```