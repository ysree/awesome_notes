# 🔐 Login Feature – Comprehensive Test Cases

## 🧩 Overview
This document lists **comprehensive test cases** for validating the **Login functionality** in both **UI and API** contexts.  
It covers functional, negative, boundary, and security scenarios to ensure the system is robust and secure.

---

## ✅ **Functional Test Cases**

| **Test Case ID** | **Scenario / Description** | **Test Steps** | **Test Data** | **Expected Result** |
|------------------|----------------------------|----------------|----------------|----------------------|
| TC001 | Valid login with correct credentials | Enter valid username and password | `user@example.com` / `Password@123` | Login successful, redirect to dashboard |
| TC002 | Login with valid credentials and “Remember Me” checked | Enable “Remember Me”, login, close browser, reopen | — | User remains logged in |
| TC003 | Login with valid credentials and then logout | Login → Logout → Try accessing dashboard | — | Redirected to login page |
| TC004 | Login with different roles | Test login for `Admin`, `User`, `Guest` roles | Different credentials | Appropriate dashboard shown per role |
| TC005 | Login after password change | Change password and login with new one | `user@example.com` / `NewPass@123` | Login success with new password only |

---

## ❌ **Negative Test Cases**

| **Test Case ID** | **Scenario / Description** | **Test Steps** | **Test Data** | **Expected Result** |
|------------------|----------------------------|----------------|----------------|----------------------|
| TC006 | Blank username and password | Leave both fields empty | — | Validation: “Username and password required” |
| TC007 | Blank username only | Enter password only | `""` / `Password@123` | Validation: “Username required” |
| TC008 | Blank password only | Enter username only | `user@example.com` / `""` | Validation: “Password required” |
| TC009 | Invalid username format | Enter invalid email format | `userexample.com` / `Password@123` | Validation: “Invalid email format” |
| TC010 | Invalid password | Enter correct username, wrong password | `user@example.com` / `WrongPass` | Error: “Invalid username or password” |
| TC011 | Leading/trailing spaces in username | Add spaces before/after | `" user@example.com "` | System trims spaces; login succeeds |
| TC012 | Password case sensitivity | Change password case | `user@example.com` / `password@123` | Login fails |
| TC013 | SQL Injection in username | `' OR 1=1--` | `' OR 1=1--` / `anything` | Login fails, no SQL execution |
| TC014 | JavaScript/XSS input in username | `<script>alert(1)</script>` | — | Input sanitized, no alert triggered |
| TC015 | Extremely long username | 256+ characters | Long email string | Validation error: “Username too long” |
| TC016 | Extremely long password | 256+ characters | Valid username / long password | Validation error: “Password too long” |
| TC017 | Password too short | < 6 characters | `user@example.com` / `abc` | Validation: “Password must be at least 6 characters” |
| TC018 | Username with special characters | `user@!#$%.com` | — | Validation: “Invalid email format” |
| TC019 | Password with all special characters | `!@#$%^&*()` | — | Accepted if policy allows special chars |
| TC020 | Unicode / non-ASCII username | `ユーザー@例.com` | — | System either supports or rejects with validation error |
| TC021 | Emoji input | `😀@mail.com` / `Password@123` | — | Validation: “Invalid username” |
| TC022 | Whitespace-only input | `"     "` | — | Validation: “Username and password required” |
| TC023 | Login with deactivated user | Use inactive account | `inactive@example.com` / `Password@123` | Error: “Account disabled” |
| TC024 | Login with locked user | Try locked account | `locked@example.com` / `Password@123` | Error: “Account locked” |
| TC025 | Brute-force attempt | Try invalid password 5+ times | — | Account temporarily locked or captcha shown |

---

## ⚙️ **API Test Cases**

| **Test Case ID** | **Scenario / Description** | **API Endpoint** | **Method** | **Expected Response** |
|------------------|----------------------------|------------------|------------|------------------------|
| API001 | Valid login request | `/api/v1/login` | `POST` | `200 OK` + token |
| API002 | Invalid credentials | `/api/v1/login` | `POST` | `401 Unauthorized` |
| API003 | Missing fields in JSON | `/api/v1/login` | `POST` | `400 Bad Request` |
| API004 | Invalid JSON payload | `/api/v1/login` | `POST` | `400 Bad Request` |
| API005 | Password too short | `/api/v1/login` | `POST` | `422 Validation Error` |
| API006 | Username field blank | `/api/v1/login` | `POST` | `400 Bad Request` |
| API007 | Login with expired account | `/api/v1/login` | `POST` | `403 Forbidden` |
| API008 | Validate token expiry | `/api/v1/token` | `POST` | Token expires correctly |
| API009 | Login without HTTPS | `/api/v1/login` | `POST` | Redirected to HTTPS |
| API010 | Validate rate limit | Send 100+ requests/min | `POST` | `429 Too Many Requests` |

---

## 🧠 **Security Test Cases**

| **Test Case ID** | **Scenario / Description** | **Expected Result** |
|------------------|----------------------------|----------------------|
| SEC001 | Password stored in DB | Password encrypted using bcrypt/SHA256 |
| SEC002 | Password not shown in logs | Logs should mask password |
| SEC003 | No session fixation | Session ID changes after login |
| SEC004 | CSRF protection on login | Requests without CSRF token rejected |
| SEC005 | Session timeout | Idle session expires after set duration |
| SEC006 | Token reuse prevention | Old JWT tokens invalid post logout |
| SEC007 | Secure cookie flags | Cookies have `Secure` and `HttpOnly` attributes |
| SEC008 | Verify failed logins logged | Each failed attempt recorded for audit |
| SEC009 | Check account lockout threshold | Lockout after 5 failed attempts |
| SEC010 | Prevent user enumeration | Same error message for invalid user and password |

---

## 🧮 **Boundary and Input Validation Cases**

| **Test Case ID** | **Scenario / Description** | **Input Example** | **Expected Result** |
|------------------|----------------------------|------------------|----------------------|
| BND001 | Minimum username length | `a@b.c` | Validation: “Username too short” |
| BND002 | Maximum username length | 255 chars | Validation: “Username too long” |
| BND003 | Minimum password length | 6 chars | Validation passes if >= policy |
| BND004 | Maximum password length | 255 chars | Validation passes or error shown |
| BND005 | Password without uppercase | `password123!` | Validation: “Must include uppercase” |
| BND006 | Password without lowercase | `PASSWORD123!` | Validation: “Must include lowercase” |
| BND007 | Password without number | `Password!` | Validation: “Must include a number” |
| BND008 | Password without special char | `Password123` | Validation: “Must include special character” |
| BND009 | Password with spaces | `Pass word@123` | Allowed or error depending on policy |
| BND010 | Password with Unicode chars | `Pässwørd@123` | System accepts or rejects gracefully |

---

## 📊 **Performance Test Cases**

| **Test Case ID** | **Scenario / Description** | **Expected Behavior** |
|------------------|----------------------------|------------------------|
| PERF001 | Response time under normal load | Avg < 500 ms |
| PERF002 | Response time under stress (1000 users) | 95th percentile < 1 sec |
| PERF003 | Database connection pooling | No deadlocks or connection exhaustion |
| PERF004 | Rate limiting and throttling | Controlled at API gateway |
| PERF005 | Token issuance speed | JWT generated in < 100 ms |

---

## 🧩 **Post-Login Validation**

- Correct user profile loads after login.  
- Role-based permissions verified.  
- JWT or session ID created and stored securely.  
- Logout invalidates session.  
- Browser back button does not reopen previous session.  
- Multi-tab login consistency validated.  
- User can reset password from login screen.

---

## 🧾 **Sample API Response**

```json
{
  "status": "success",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
  "expires_in": 3600,
  "user": {
    "id": "U12345",
    "email": "user@example.com",
    "role": "user"
  }
}
