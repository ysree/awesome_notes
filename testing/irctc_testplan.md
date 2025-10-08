2. Create Testcases for Railway Booking App 

# Test Cases: Railway Booking Application

#### 1. User Registration & Login

| Test Case ID | Description | Steps | Expected Result | Status |
|--------------|------------|-------|----------------|--------|
| TC-001 | Register new user | Navigate to registration page → Enter valid details → Submit | User account created successfully; confirmation email sent | Pass/Fail |
| TC-002 | Login with valid credentials | Go to login page → Enter registered username & password → Submit | User successfully logged in and redirected to dashboard | Pass/Fail |
| TC-003 | Login with invalid credentials | Enter wrong username/password → Submit | Display error message “Invalid credentials” | Pass/Fail |
| TC-004 | Password recovery | Click on “Forgot Password” → Enter registered email → Submit | Password reset email sent successfully | Pass/Fail |

---

#### 2. Search Trains

| Test Case ID | Description | Steps | Expected Result | Status |
|--------------|------------|-------|----------------|--------|
| TC-005 | Search train between two stations | Enter Source, Destination, Date → Click Search | List of available trains displayed with details (time, class, availability) | Pass/Fail |
| TC-006 | Search with invalid station codes | Enter non-existent station codes → Search | Display error message “No trains available” | Pass/Fail |
| TC-007 | Search with past date | Enter past date → Search | Display error message “Invalid travel date” | Pass/Fail |

---

#### 3. Seat Availability & Booking

| Test Case ID | Description | Steps | Expected Result | Status |
|--------------|------------|-------|----------------|--------|
| TC-008 | Check seat availability | Select train → Select class → Check availability | Correct seat availability displayed | Pass/Fail |
| TC-009 | Book ticket with valid details | Select train & class → Enter passenger details → Make payment → Confirm | Ticket booked successfully; PNR generated | Pass/Fail |
| TC-010 | Attempt booking with invalid payment | Repeat above steps with invalid card/insufficient funds | Payment failed; booking not confirmed; display error | Pass/Fail |
| TC-011 | Book ticket for multiple passengers | Select train → Enter multiple passenger details → Payment → Confirm | Tickets booked for all passengers; separate seat numbers assigned | Pass/Fail |

---

#### 4. Ticket Cancellation & Refund

| Test Case ID | Description | Steps | Expected Result | Status |
|--------------|------------|-------|----------------|--------|
| TC-012 | Cancel booked ticket | Go to booked tickets → Select PNR → Click Cancel | Ticket cancelled; refund processed according to policy | Pass/Fail |
| TC-013 | Attempt cancellation after cutoff | Try to cancel within non-refundable window | Display error message “Cancellation not allowed” | Pass/Fail |

---

#### 5. PNR & Booking History

| Test Case ID | Description | Steps | Expected Result | Status |
|--------------|------------|-------|----------------|--------|
| TC-014 | Check PNR status | Enter PNR → Submit | Display booking details and current status (Confirmed, Waitlist, RAC) | Pass/Fail |
| TC-015 | View booking history | Login → Go to Booking History | List of all past and upcoming bookings displayed | Pass/Fail |

---

#### 6. Notifications

| Test Case ID | Description | Steps | Expected Result | Status |
|--------------|------------|-------|----------------|--------|
| TC-016 | Email/SMS on booking | Book ticket successfully | Receive confirmation email/SMS with ticket details | Pass/Fail |
| TC-017 | Alerts for train status changes | Login → Enable notifications → Train delayed/cancelled | Receive alert notification about status change | Pass/Fail |

---

#### 7. Security & Validation

| Test Case ID | Description | Steps | Expected Result | Status |
|--------------|------------|-------|----------------|--------|
| TC-018 | Prevent SQL Injection | Enter SQL code in input fields → Submit | Input sanitized; no database compromise | Pass/Fail |
| TC-019 | Session timeout | Login → Remain inactive → Perform action after timeout | User redirected to login page | Pass/Fail |
| TC-020 | Data validation | Enter invalid passenger info (name, age, email) | Display proper error messages; booking not allowed | Pass/Fail |
