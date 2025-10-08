
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
