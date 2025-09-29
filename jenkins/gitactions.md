# GitHub Actions Notes

## 1. What is GitHub Actions?

* GitHub Actions is a **CI/CD tool** built into GitHub for automating workflows.
* Allows **building, testing, and deploying code** directly from a GitHub repository.
* Workflows are triggered by **events** (push, pull request, release, schedule, or manually).

---

## 2. Key Concepts

| Term         | Description                                                                                  |
| ------------ | -------------------------------------------------------------------------------------------- |
| **Workflow** | Automated process defined in `.github/workflows/` as a YAML file. Can include multiple jobs. |
| **Job**      | Set of steps running on the same runner.                                                     |
| **Step**     | Single task in a job, e.g., running a script or action.                                      |
| **Action**   | Reusable unit of code performing a specific task (from marketplace or custom).               |
| **Runner**   | Server that executes jobs (GitHub-hosted or self-hosted).                                    |
| **Event**    | Trigger for the workflow (push, pull\_request, schedule, workflow\_dispatch).                |
| **Secrets**  | Encrypted variables stored in GitHub for sensitive data like API keys.                       |

---

## 3. Workflow File Structure

```yaml
name: CI Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
  schedule:
    - cron: '0 2 * * *'

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'

      - name: Build with Maven
        run: mvn clean install

      - name: Run tests
        run: mvn test

      - name: Upload test results
        uses: actions/upload-artifact@v3
        with:
          name: test-results
          path: target/surefire-reports/
```

---

## 4. Common Use Cases

1. **Continuous Integration (CI)** – Run tests automatically on push or PR.
2. **Continuous Deployment (CD)** – Deploy apps automatically to AWS, Azure, GCP, or Kubernetes.
3. **Code Quality Checks** – Run SonarQube scans, Jacoco coverage, or OWASP Dependency-Check.
4. **Automated Security Scans** – SAST, dependency vulnerability checks.
5. **Notifications** – Send Slack/email notifications on workflow events.
6. **Scheduled Jobs** – Run nightly builds, backups, or maintenance scripts.

---

## 5. Advantages

* Directly integrated into GitHub.
* No need for external CI/CD server (can use self-hosted runners if needed).
* Supports **matrix builds** for multiple OS, language versions, or configurations.
* Reusable **actions** available from GitHub Marketplace.
* Easy to chain multiple workflows (build → test → deploy).

---

## 6. Best Practices

* Use **separate workflows** for CI, CD, and scheduled tasks.
* Store sensitive info in **GitHub Secrets**.
* Reuse actions from **GitHub Marketplace** to reduce complexity.
* Keep workflows modular and use **matrix strategies** for multi-environment testing.
* Monitor workflow runtime and avoid unnecessary long-running jobs.
