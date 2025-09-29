# Complete CI/CD Pipeline

## 1. Code Commit

* Developers push code to a **version control system** (Git, GitHub, GitLab, Bitbucket).
* Each commit **triggers the CI/CD pipeline** automatically.
* Best practices:

  * Commit small, atomic changes.
  * Use feature branches and pull requests.
  * Include descriptive commit messages.

---

## 2. Compile & Build

* Source code is compiled and packaged into artifacts.
* Tools: Maven, Gradle (Java), NPM/Yarn (Node.js), Make (C/C++).
* Example: `mvn clean package` or `gradle build` for Java.
* Output: `.jar`, `.war`, `.zip`, or other deployable files.

---

## 3. Static Code Analysis

* Inspects **source code for bugs, code smells, and security issues** without execution.
* Tools: **SonarQube, ESLint, PMD, Checkstyle, SpotBugs**.
* Benefits:

  * Detects code quality issues early.
  * Enforces coding standards.
  * Integrates with CI/CD for automated checks.
* Typical workflow:

```bash
mvn sonar:sonar \
  -Dsonar.projectKey=my-project \
  -Dsonar.host.url=http://sonarqube-server:9000 \
  -Dsonar.login=<SONAR_TOKEN>
```

---

## 4. Code Coverage

* Measures **how much of the code is exercised by automated tests**.
* Tools: **Jacoco (Java), Istanbul/nyc (JavaScript), Coverage.py (Python)**.
* Example (Jacoco + Maven):

```bash
mvn clean test jacoco:report
```

* Coverage reports include:

  * Line coverage
  * Branch coverage
  * Method coverage
* Often integrated with SonarQube for visualization.

---

## 5. SonarQube Analysis

* Performs **deep static analysis** with security, maintainability, and reliability checks.
* Key metrics:

  * **Bugs**: Functional issues
  * **Vulnerabilities**: Security risks
  * **Code smells**: Maintainability issues
  * **Coverage**: % of code tested
* Integration with CI/CD ensures **failed quality gates block deployment**.

---

## 6. Scan for Dependency Vulnerabilities

* Detects vulnerabilities in **third-party libraries or dependencies**.
* Tools: **OWASP Dependency-Check, Snyk, WhiteSource**.
* Example:

```bash
dependency-check.sh --scan ./ --format HTML --out reports/dependency-check-report.html
```

* Benefits:

  * Prevents known vulnerable libraries in production.
  * Ensures compliance with security policies.

---

## 7. Artifact Scan

* Scan **build artifacts (.jar, .war, .zip)** for vulnerabilities.
* Tools: **JFrog Xray, Anchore, Clair**.
* Ensures artifacts are **secure and free from malicious code** before deployment.

---

## 8. Docker Image Scan

* Scan Docker containers for **vulnerabilities in base images and application layers**.
* Tools: **Trivy, Anchore, Clair, Docker Scan**.
* Example:

```bash
trivy image myapp:latest
```

* Prevents deploying containers with **critical security issues**.

---

## 9. Push Artifacts to Repository

* Upload build artifacts and Docker images to repositories for **centralized storage and versioning**.
* Artifact Repositories: **JFrog Artifactory, Nexus, GitHub Packages, Docker Hub**.
* Example:

```bash
# Push JAR to Artifactory
curl -u user:password -T target/myapp.jar "https://artifactory.example.com/libs-release-local/myapp/1.0.0/myapp-1.0.0.jar"

# Push Docker image
docker tag myapp:1.0.0 artifactory.example.com/myapp:1.0.0
docker push artifactory.example.com/myapp:1.0.0
```

---

## 10. Notifications

* Notify teams of build, test, or deployment results.
* Channels: **Slack, Email, Microsoft Teams, Jira**.
* Example (Slack in CI/CD):

```yaml
- name: Slack Notification
  uses: slackapi/slack-github-action@v1.23.0
  with:
    channel-id: C12345678
    text: "Build ${{ github.run_number }} completed successfully!"
    token: ${{ secrets.SLACK_TOKEN }}
```

---

## 11. Deployment

* Deploy artifacts or Docker images to environments: **Dev, QA, Staging, Production**.
* Tools: **Kubernetes, Helm, Terraform, AWS CodeDeploy, Ansible**.
* Example (Kubernetes + Helm):

```bash
helm upgrade --install myapp ./charts/myapp \
  --namespace prod \
  --set image.tag=1.0.0
```

---

## 12. Monitoring

* Monitor applications for **performance, errors, and availability** post-deployment.
* Tools: **Prometheus, Grafana, ELK Stack, Datadog, New Relic**.
* Metrics:

  * CPU & memory usage
  * Request latency and throughput
  * Error rates and logs
* Alerts notify teams if thresholds are exceeded.

---

## 13. Summary Workflow Diagram

```
Code Commit
      |
      v
Compile & Build
      |
      v
Static Code Analysis (SonarQube)
      |
      v
Unit Tests & Code Coverage (Jacoco)
      |
      v
Dependency Vulnerability Scan (OWASP, Snyk)
      |
      v
Artifact & Docker Scan (Trivy, Xray)
      |
      v
Push Artifacts & Docker Image to Repository
      |
      v
Notifications (Slack, Email)
      |
      v
Deployment (K8s, Helm, AWS)
      |
      v
Monitoring & Feedback (Prometheus, Grafana)
```
