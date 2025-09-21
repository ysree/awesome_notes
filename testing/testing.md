# Approach for testing the product.
## Structured, step-by-step approach
1. **Understand the product**:
Before testing, gather all relevant documentation: functional specifications, user stories, use cases, and design documents. Understand the product’s purpose, target audience, and expected behavior.

2. **Define the testing objectives**:
Decide what you need to validate. Objectives can include **functional correctness, performance, security, usability, and compatibility**. Clearly define success criteria.

3. **Identify the types of testing needed**:
    - **Functional testing**: Verify features work as expected.
    - **Integration testing**: Ensure modules work together.
    - **System testing**: Validate the complete product in an environment similar to production.
    - **Regression testing**: Ensure new changes don’t break existing functionality.
    - **Performance testing**: Check speed, responsiveness, and scalability.
    - **Security testing**: Identify vulnerabilities and access control issues.
    - **User acceptance testing (UAT)**: Verify the product meets user expectations.

4. **Prepare the test plan**:
Document the **scope, testing types, entry/exit criteria, resources, timelines, and risk assessment**. Include the testing environment setup and tools needed.

5. **Design test cases and scenarios:**
Create detailed test cases covering all **features and edge cases**. Include **expected inputs, actions, and expected outcomes**. Prioritize critical paths and high-risk areas.

6. **Set up the test environment**:
Ensure the **hardware, software, network, and any other dependencies** match the intended environment. This could include staging servers, databases, and third-party services.

7. **Execute tests**:
Run manual or automated tests according to the plan. Record **results accurately, noting failures, bugs, or deviations** from expected behavior.

8. **Report and track defects**:
Log defects in a tracking system with clear **reproduction steps, screenshots, and severity levels**. Collaborate with the development team for fixes.

9. **Retest and regression**:
After fixes, retest the affected areas and perform regression testing to ensure no other part of the product is broken.

10. **Review and closure**:
Summarize **testing results, coverage, defect trends, and overall product quality**. Confirm that objectives are met and report readiness for release.

11. **Continuous improvement**:
Analyze testing challenges and gaps for future projects. Update test cases and strategies based on lessons learned.

----

# How to validate if two files are the same or not? What attributes of a file can be checked for validation? [e.g. metadata, content]

#### Check file attributes (quick check)
- Before comparing the contents, you can check metadata. Key attributes include:
- File size – If sizes differ, files are definitely not the same.
- File type – Ensure both are the same type (text, binary, etc.).
- Modification timestamp – Can hint if files have changed.
- Permissions/ownership – Can help in certain contexts, but not definitive for content.
- Checksum or hash – Generate a hash (MD5, SHA1, SHA256) for each file; if hashes match, files are - very likely identical.
- Actual content comparison (diff, cmp)

--

Given file is in a distributed system and file is open at one node for writing and that file is not allowed to be deleted on any other node. Write the test plans for the same.

---
# Test Plan: Prevent Deletion of Open File in Distributed System

### 1. Objective
To validate that when a file is open for writing on one node in the distributed system, it cannot be deleted from any other node until it is closed. This ensures data integrity and consistency across the system.

### 2. Scope
- Covers file operations (open, write, delete) across multiple nodes.  
- Focus on file locking, distributed metadata consistency, and deletion restrictions.  
- Applies to shared/distributed filesystems (e.g., NFS, GlusterFS, CephFS, HDFS).  

### 3. Assumptions
- The distributed filesystem provides file locking mechanisms.  
- All nodes share consistent metadata.  
- Delete operation must respect open handles on other nodes.  

### 4. Preconditions
- Distributed system setup with at least 2 nodes.  
- Test file (`testfile.txt`) created and accessible from all nodes.  
- Proper logging and monitoring enabled on each node.  

### 5. Test Scenarios & Steps

#### Scenario 1: Delete attempt while file is open for writing
1. On Node A, open `testfile.txt` in write mode (vim, nano, or programmatic open).  
2. On Node B, attempt to delete the same file (`rm testfile.txt`).  
**Expected Result:** Deletion must fail with a proper error (e.g., *Device or resource busy*, *Permission denied*). File remains accessible on Node A.  

#### Scenario 2: Delete attempt after closing the file
1. On Node A, open and then close `testfile.txt` after writing.  
2. On Node B, attempt to delete `testfile.txt`.  
**Expected Result:** Deletion succeeds because file is no longer open.  

#### Scenario 3: Concurrent write + delete attempts
1. On Node A, start a continuous write process (`dd if=/dev/zero of=testfile.txt bs=1M count=100`).  
2. On Node B, simultaneously attempt to delete the file.  
**Expected Result:** Deletion fails until Node A completes and closes the file.  

#### Scenario 4: Multiple nodes access
1. On Node A, open file for writing.  
2. On Node B, open the same file for reading.  
3. On Node C, attempt to delete the file.  
**Expected Result:** Deletion must fail as long as Node A has the file open for writing.  

#### Scenario 5: Edge case – Force delete
1. On Node A, open file for writing.  
2. On Node B, attempt to use `unlink()` or `rm -f`.  
**Expected Result:** Operation must fail or be blocked; file stays intact.  

#### Scenario 6: System crash during write
1. On Node A, open file for writing but do not close. Simulate crash or network disconnection.  
2. On Node B, attempt to delete the file.  
**Expected Result:** Depending on system design – deletion should either fail until a timeout/lease expiry or succeed if the lock is released by failure detection.  

### 6. Validation Checks
- File content is not corrupted after delete attempts.  
- Error messages are clear and consistent across nodes.  
- Metadata (inode reference count, locks) is correctly updated.  
- Logs show proper rejection of delete requests during write locks.  

### 7. Pass/Fail Criteria
- **Pass:** Deletion is not allowed when the file is open for writing, and allowed after the file is closed.  
- **Fail:** File gets deleted while still open for writing, or system behavior is inconsistent across nodes.  

### 8. Tools Needed
- `ls -l`, `lsof`, `fuser` for checking open file handles.  
- Logging/monitoring tools for distributed FS.  
- Scripts to automate open-write-delete operations across nodes.  

---

# Suppose you are moving a file from one server to client and file transfer speed is very slow. How will you troubleshoot for the same?

#### 1. Check the File Transfer Method
- Identify how the file is being moved: scp, rsync, sftp, NFS mount, HTTP, FTP, etc.
- **Some tools/protocols add overhead** (e.g., encryption in scp/sftp). For testing, try a lighter protocol (e.g., rsync --inplace --whole-file or plain nc).
#### 2. Check Network Performance
- Use ping to check latency: `ping <server_ip>`
- Use `traceroute` or `mtr` to detect hops causing delay.
- Measure raw network bandwidth with tools like `iperf3`:
    - `iperf3 -s   # On server`
    - `iperf3 -c <server_ip>   # On client`
- Look for packet loss or very high latency.
#### 3. Check Server and Client Resources
- CPU and memory usage (e.g., `top`, `htop`, `vmstat`).
- Disk I/O bottlenecks: `iostat -x 1`
- Network interface utilization: `ifstat` `sar -n DEV 1`
#### 4. Ensure NIC isn’t stuck at 100 Mbps instead of 1 Gbps/10 Gbps: 
-  `ethtool eth0`
#### 5. Check File Size and Disk Throughput
- If file is very large, the disk’s read/write speed can be a bottleneck.
- Test raw disk performance with `dd` or `fio`:
    - `dd if=/dev/zero of=testfile bs=1G count=1 oflag=direct`
#### 6. Check Protocol-Level Bottlenecks
- For SSH-based transfers, encryption can slow things down:
    - Try weaker cipher for testing:  `scp -c aes128-ctr file user@host:/path/`
- For NFS, check mount options (`rsize`, `wsize`, `tcp/udp`).
- For HTTP/FTP, confirm server process limits (connection limits, throttling).
#### 7. Tune protocol/TCP buffers and settings

---
# Create Test Plan for firmware upgrade 
# Test Plan: Firmware Upgrade

#### 1. Objective
To validate that the firmware upgrade process completes successfully without data loss, system crashes, or functionality degradation. Ensure rollback works in case of failures.

---

#### 2. Scope
- Covers firmware upgrade on devices (routers, IoT devices, servers, embedded systems).
- Includes pre-upgrade checks, upgrade process validation, and post-upgrade verification.
- Excludes hardware failures unrelated to firmware.

---

#### 3. Assumptions
- Firmware image is verified and compatible with the device.
- Backup of existing firmware and configuration is available.
- Device has sufficient power and storage for upgrade.

---

#### 4. Preconditions
- Device powered on and reachable.
- Proper network connection if upgrade is over-the-air (OTA).
- Admin privileges for performing upgrade.
- Logs and monitoring enabled.

---

#### 5. Test Scenarios & Steps

##### Scenario 1: Pre-upgrade Verification
- Verify current firmware version.
- Check device storage and battery/power status.
- Ensure backup of current firmware/configuration.
**Expected Result:** All checks pass; device ready for upgrade.

##### Scenario 2: Upgrade Execution
- Apply firmware upgrade via supported method (OTA, USB, manual).
- Monitor upgrade progress.
**Expected Result:** Firmware upgrade completes without errors; device reboots successfully.

##### Scenario 3: Post-upgrade Verification
- Verify new firmware version.
- Check system logs for errors during upgrade.
- Test device functionality (network, services, I/O).
**Expected Result:** Device operates normally; all features functional.

##### Scenario 4: Upgrade Failure Handling
- Simulate interruption (power loss, network failure) during upgrade.
- Attempt automatic or manual rollback.
**Expected Result:** Device restores previous firmware without corruption.

##### Scenario 5: Stress/Load Testing Post Upgrade
- Execute heavy usage scenarios to ensure stability.
**Expected Result:** No crashes, data loss, or performance degradation.

##### Scenario 6: Security Verification
- Check firmware signature and integrity.
- Validate that upgrade process is secure (no unauthorized modification).
**Expected Result:** Firmware integrity verified; no security vulnerabilities introduced.

---

#### 6. Validation Checks
- Firmware version correctly updated.
- Device functionality unaffected.
- Logs show successful upgrade and no errors.
- Rollback works in case of failures.
- Network and system services operational.

---

#### 7. Pass/Fail Criteria
- **Pass:** Device upgraded successfully, all features functional, rollback works if required.
- **Fail:** Upgrade fails, device crashes, or functionality is lost after upgrade.

---

#### 8. Tools Needed
- Firmware upgrade tools/software.
- Device monitoring and logging tools.
- Network connectivity and power monitoring tools.
- Automated test scripts for post-upgrade functionality.

---

#### 9. Risks & Mitigation
- **Risk:** Power failure during upgrade.  
  **Mitigation:** Ensure backup power or UPS.
- **Risk:** Network failure during OTA upgrade.  
  **Mitigation:** Retry mechanism or local upgrade option.
- **Risk:** Corrupted firmware image.  
  **Mitigation:** Verify checksum before upgrade.

---
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

---

# ✅ Test Cases for SCP (Secure Copy Protocol)

SCP is used to securely transfer files between hosts over SSH.  
Test cases cover **functionality, security, performance, and error handling**.

---

#### 1. Basic File Transfer
- **TC1:** Copy a small file from local to remote host.  
- **TC2:** Copy a file from remote host to local.  
- **TC3:** Copy a file between two remote hosts (using local as jump).  

---

#### 2. Directory Transfer
- **TC4:** Copy an entire directory recursively with `-r`.  
- **TC5:** Copy hidden files and directories.  

---

#### 3. File Integrity
- **TC6:** Verify checksum (e.g., `md5sum`) of source and destination file matches.  
- **TC7:** Copy large file (GBs) and confirm no corruption.  

---

#### 4. Authentication
- **TC8:** SCP with password-based authentication.  
- **TC9:** SCP with SSH key-based authentication.  
- **TC10:** SCP with invalid credentials → should fail.  

---

#### 5. Permissions & Ownership
- **TC11:** Copy file as normal user to a location requiring root → should fail.  
- **TC12:** Preserve timestamps and permissions using `-p`.  
- **TC13:** Transfer with `sudo scp` to restricted directories.  

---

#### 6. Error Handling
- **TC14:** Copy a non-existent file → should show error.  
- **TC15:** Destination path invalid → should show error.  
- **TC16:** Network disconnect during transfer → should fail gracefully.  

---

#### 7. Security
- **TC17:** Verify SCP uses SSH encryption (check with `tcpdump` or logs).  
- **TC18:** Ensure no plaintext password exposure.  
- **TC19:** Test SCP with strict host key checking (`-o StrictHostKeyChecking=yes`).  

---

#### 8. Performance
- **TC20:** Measure transfer speed for different file sizes.  
- **TC21:** Compare performance with and without compression (`-C`).  
- **TC22:** Parallel transfers from multiple clients.  

---

#### 9. Compatibility
- **TC23:** SCP between different OS (Linux ↔ Windows, Linux ↔ macOS).  
- **TC24:** SCP with different SSH versions.  

---

#### 10. Edge Cases
- **TC25:** Copy zero-byte file.  
- **TC26:** Copy file with special characters in filename.  
- **TC27:** Copy very large directory with thousands of files.  
- **TC28:** Copy file with very long path names.

----
# Soda Dispensing Machine Scenario

#### 1. Objective
Simulate and test a **soda vending machine** that dispenses beverages based on user selection and payment.  
Ensure correct behavior for various inputs, payments, and stock conditions.

---

#### 2. Features
1. **User Interaction**
   - Display available soda types and prices.
   - Accept user input for soda selection.

2. **Payment Handling**
   - Accept coins or digital payments.
   - Validate payment amount.
   - Return change if payment exceeds cost.

3. **Inventory Management**
   - Track stock of each soda type.
   - Notify when stock is low or out of stock.
   - Prevent dispensing if stock is insufficient.

4. **Dispensing**
   - Dispense the selected soda if payment is sufficient and stock is available.
   - Display confirmation after successful dispensing.

5. **Error Handling**
   - Insufficient payment → display error.
   - Out of stock → display error.
   - Invalid selection → display error.

---

#### 3. Functional Flow
1. User selects a soda from available options.  
2. Machine prompts for payment.  
3. User inserts coins or pays digitally.  
4. Machine checks:
   - Is the payment sufficient?  
   - Is the selected soda in stock?  
5. If yes → dispense soda and return change if needed.  
6. If no → display appropriate error message.

---

#### 4. Test Cases

| Test Case ID | Scenario | Input | Expected Result |
|--------------|---------|-------|----------------|
| TC1 | Valid selection & exact payment | Select Cola, insert $1 | Dispense Cola, no change |
| TC2 | Valid selection & excess payment | Select Sprite, insert $2 | Dispense Sprite, return $1 change |
| TC3 | Valid selection & insufficient payment | Select Fanta, insert $0.5 | Error: Insufficient payment |
| TC4 | Out of stock selection | Select Pepsi (stock=0) | Error: Out of stock |
| TC5 | Invalid selection | Select Lemonade (not available) | Error: Invalid selection |
| TC6 | Multiple purchases sequentially | Select Cola, pay, then select Fanta | Each purchase handled independently |
| TC7 | Low stock warning | Stock=1 for Sprite, select Sprite | Dispense Sprite, show low stock warning |
| TC8 | Cancel transaction before payment | Select Coke, cancel | Transaction canceled, no payment deducted |
| TC9 | Digital payment | Select Pepsi, pay via card | Dispense Pepsi, verify digital payment processed |
| TC10 | Machine resets after failure | Simulate power outage | Machine restores inventory and functionality after restart |

---

#### 5. Edge Cases
- Multiple users trying to purchase at the same time.  
- Invalid coin insertion (fake or wrong denomination).  
- Network failure during digital payment.  
- Machine running out of change for excess payment.  

---

#### 6. Non-Functional Considerations
- **Performance:** Dispense soda within 5 seconds after payment.  
- **Reliability:** System should handle 1000 transactions/day without failure.  
- **Usability:** Clear user interface and error messages.  
- **Security:** Prevent tampering with payment and inventory systems.  

---

#### Summary
The **Soda Dispensing Machine scenario** covers **functional and non-functional requirements, user interactions, payment handling, inventory management, error handling, and edge cases**.  
Testing ensures **accuracy, usability, and reliability** of the vending machine system.
