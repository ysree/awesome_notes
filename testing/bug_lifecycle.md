# 🐞 Software Bug Life Cycle (Defect Life Cycle)

The **Bug Life Cycle** (or **Defect Life Cycle**) describes the journey of a software defect — from its discovery to its closure.  
Each stage ensures the bug is properly identified, prioritized, fixed, tested, and tracked.

---

## 🔁 Stages of the Bug Life Cycle

| **Stage** | **Description** | **Responsible Role** |
|------------|-----------------|----------------------|
| **New / Opened** | A tester finds a bug and reports it with detailed steps, environment, severity, and screenshots/logs. | Tester |
| **Assigned** | The bug is assigned to a developer for fixing by the team lead or test manager. | Test Lead / Manager |
| **In Progress / Fixed** | The developer works on the issue and updates the status to *In Progress*. Once resolved, it’s marked as *Fixed*. | Developer |
| **Code Review / Peer Review** | The fix is reviewed by another developer or reviewer to ensure correctness and adherence to standards. | Peer Developer / Reviewer |
| **Retest / Ready for QA** | The tester re-tests the application in the next test cycle or build to verify the fix. | Tester |
| **Verified** | The tester confirms the issue is resolved and no longer reproducible. | Tester |
| **Closed** | Once verified, the bug is marked *Closed* in the bug tracking tool (e.g., JIRA, Bugzilla). | Tester / QA Lead |
| **Reopened** | If the issue reappears after being marked fixed or closed, it’s *Reopened* and the cycle continues. | Tester |
| **Deferred / Postponed** | If the fix is not critical or can wait for a future release, it’s deferred. | Product Owner / Manager |
| **Duplicate / Rejected** | If the bug is already logged or not valid (e.g., expected behavior), it’s marked as *Duplicate* or *Rejected*. | Developer / Lead |

---

## 🧭 Typical Flow Diagram

