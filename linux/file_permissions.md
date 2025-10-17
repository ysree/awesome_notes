# Linux File Permissions - Quick Reference

## Permission Overview

### Permission Types
| Permission | Symbol | Numeric | File Effect | Directory Effect |
|------------|---------|---------|-------------|------------------|
| **Read** | `r` | 4 | View file content | List directory contents |
| **Write** | `w` | 2 | Modify file content | Create/delete files in directory |
| **Execute** | `x` | 1 | Run file as program | Enter/traverse directory |

### User Categories
| Category | Symbol | Description |
|----------|---------|-------------|
| **User/Owner** | `u` | File owner |
| **Group** | `g` | File group members |
| **Others** | `o` | Everyone else |
| **All** | `a` | All users (u+g+o) |

---

## Reading Permissions

### Long Format Display
```bash
ls -l filename
# Output: -rwxr-xr-- 1 user group 1024 Jan 15 10:30 filename
#         ^^^^^^^^^
#         permissions
```

### Permission Breakdown
```
-rwxr-xr--
^ ^^^ ^^^ ^^^
| ||| ||| |||
| ||| ||| ++-- Others: r-- (read only)
| ||| +++------ Group:  r-x (read + execute)
| +++---------- User:   rwx (read + write + execute)
+-------------- File type: - (regular file)
```

### File Type Indicators
| Symbol | Type |
|--------|------|
| `-` | Regular file |
| `d` | Directory |
| `l` | Symbolic link |
| `b` | Block device |
| `c` | Character device |
| `p` | Named pipe |
| `s` | Socket |

---

## Numeric (Octal) Permissions

### Permission Values
| Binary | Octal | Permissions | Symbol |
|--------|-------|-------------|---------|
| 000 | 0 | No permissions | `---` |
| 001 | 1 | Execute only | `--x` |
| 010 | 2 | Write only | `-w-` |
| 011 | 3 | Write + Execute | `-wx` |
| 100 | 4 | Read only | `r--` |
| 101 | 5 | Read + Execute | `r-x` |
| 110 | 6 | Read + Write | `rw-` |
| 111 | 7 | All permissions | `rwx` |

### Common Permission Combinations
| Octal | Symbolic | Description | Use Case |
|-------|----------|-------------|----------|
| `755` | `rwxr-xr-x` | Owner: all, Others: read+execute | Executable files, directories |
| `644` | `rw-r--r--` | Owner: read+write, Others: read | Regular files |
| `600` | `rw-------` | Owner: read+write only | Private files |
| `777` | `rwxrwxrwx` | All permissions for everyone | **Avoid - security risk** |
| `000` | `---------` | No permissions | Locked files |

---

## Changing Permissions

### Using chmod with Numeric Mode
```bash
chmod 755 filename          # Set to rwxr-xr-x
chmod 644 *.txt             # Set all .txt files to rw-r--r--
chmod 600 private_file      # Set to rw-------
chmod -R 755 directory/     # Recursive: set directory permissions
```

### Using chmod with Symbolic Mode

#### Adding Permissions (+)
```bash
chmod u+x filename          # Add execute for user
chmod g+w filename          # Add write for group
chmod o+r filename          # Add read for others
chmod a+x filename          # Add execute for all
chmod u+rwx filename        # Add all permissions for user
```

#### Removing Permissions (-)
```bash
chmod u-x filename          # Remove execute from user
chmod g-w filename          # Remove write from group  
chmod o-r filename          # Remove read from others
chmod a-x filename          # Remove execute from all
```

#### Setting Exact Permissions (=)
```bash
chmod u=rwx filename        # Set user to rwx exactly
chmod g=r filename          # Set group to read only
chmod o= filename           # Remove all permissions from others
chmod a=r filename          # Set everyone to read only
```

#### Combining Operations
```bash
chmod u+x,g-w,o=r filename  # Multiple operations
chmod u=rwx,go=rx filename  # Set user=rwx, group+others=rx
```

---

## Ownership Commands

### Changing Owner
```bash
chown user filename         # Change owner
chown user:group filename   # Change owner and group
chown :group filename       # Change group only
chown -R user directory/    # Recursive ownership change
```

### Changing Group
```bash
chgrp group filename        # Change group
chgrp -R group directory/   # Recursive group change
```

---

## Special Permissions

### Sticky Bit (t)
```bash
chmod +t directory          # Set sticky bit
chmod 1755 directory        # Numeric: sticky bit + 755
# Effect: Only owner can delete files in directory (like /tmp)
```

### SUID (Set User ID)
```bash
chmod u+s filename          # Set SUID
chmod 4755 filename         # Numeric: SUID + 755  
# Effect: File runs with owner's privileges
```

### SGID (Set Group ID)
```bash
chmod g+s filename          # Set SGID on file
chmod g+s directory         # Set SGID on directory
chmod 2755 filename         # Numeric: SGID + 755
# Effect: File runs with group's privileges / Files inherit directory group
```

### Viewing Special Permissions
```bash
ls -l /tmp                  # Shows sticky bit: drwxrwxrwt
ls -l /usr/bin/passwd       # Shows SUID: -rwsr-xr-x
ls -l /usr/bin/write        # Shows SGID: -rwxr-sr-x
```

---

## Default Permissions (umask)

### Check Current umask
```bash
umask                       # Show current umask (e.g., 0022)
umask -S                    # Show in symbolic format
```

### Common umask Values
| umask | Files Created | Directories Created | Description |
|-------|---------------|-------------------|-------------|
| `022` | `644 (rw-r--r--)` | `755 (rwxr-xr-x)` | Default for most systems |
| `002` | `664 (rw-rw-r--)` | `775 (rwxrwxr-x)` | Group writable |
| `077` | `600 (rw-------)` | `700 (rwx------)` | Private to user |

### Set umask
```bash
umask 022                   # Set umask temporarily
echo "umask 022" >> ~/.bashrc  # Set permanently
```

---

## Quick Commands Reference

### File Permission Commands
```bash
# View permissions
ls -l filename
ls -la directory/           # Include hidden files
stat filename               # Detailed file info

# Quick permission changes
chmod +x filename           # Make executable
chmod -x filename           # Remove execute
chmod 755 filename          # Standard executable
chmod 644 filename          # Standard file
chmod 600 filename          # Private file

# Ownership changes  
sudo chown user:group file  # Change owner and group
sudo chown -R user dir/     # Recursive ownership

# Find files with specific permissions
find /path -perm 755        # Find files with exact 755
find /path -perm -644       # Find files with at least 644
find /path -perm /u+w       # Find files writable by user
```

### Permission Checking
```bash
# Test if file is readable/writable/executable
[ -r filename ] && echo "Readable"
[ -w filename ] && echo "Writable"  
[ -x filename ] && echo "Executable"

# Check who can access
namei -l /path/to/file      # Show permissions for entire path
```

---

## Common Scenarios

### Web Server Files
```bash
chmod 644 *.html            # Web pages
chmod 755 cgi-bin/          # CGI directory  
chmod 755 *.cgi             # CGI scripts
```

### SSH Keys
```bash
chmod 700 ~/.ssh            # SSH directory
chmod 600 ~/.ssh/id_rsa     # Private key
chmod 644 ~/.ssh/id_rsa.pub # Public key
chmod 644 ~/.ssh/authorized_keys
```

### Script Files
```bash
chmod +x script.sh          # Make script executable
chmod 755 /usr/local/bin/myscript  # System script
```

### Backup and Restore Permissions
```bash
# Backup permissions
getfacl -R directory > permissions.acl

# Restore permissions  
setfacl --restore=permissions.acl
```

---

## Troubleshooting Permission Issues

### Common Permission Problems
```bash
# Permission denied errors
ls -l filename              # Check current permissions
namei -l /full/path/to/file # Check entire path permissions

# Fix common issues
chmod 644 filename          # Standard file permissions
chmod 755 directory         # Standard directory permissions
sudo chown $USER filename   # Take ownership
```

### Security Best Practices
- Never use `777` permissions unless absolutely necessary
- Use `644` for regular files, `755` for directories
- Set `600` for private files (SSH keys, config files)
- Use `umask 022` for standard security
- Regularly audit file permissions with `find`

---

This reference covers the essential Linux file permission concepts and commands for daily system administration tasks.