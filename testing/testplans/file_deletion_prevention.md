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





