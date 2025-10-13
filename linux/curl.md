Here’s a **structured guide on `curl` commands** with explanations and examples. `curl` is a powerful command-line tool used to **transfer data to or from a server** using various protocols like HTTP, HTTPS, FTP, and more.

---

# **`curl` Command Notes**

---

## **1️⃣ Basic GET Request**

```bash
curl https://example.com
```

**Explanation:**

* Fetches the content of `https://example.com` and displays it in the terminal.
* Default method is **GET**.

---

## **2️⃣ Get Headers Only**

```bash
curl -I https://example.com
```

**Explanation:**

* `-I` or `--head` fetches only **HTTP headers**, not the body.
* Useful to check **status code, server type, content-type, etc.**

**Sample Output:**

```
HTTP/1.1 200 OK
Date: Mon, 13 Oct 2025 21:00:00 GMT
Content-Type: text/html; charset=UTF-8
Content-Length: 1256
Server: Apache
```

---

## **3️⃣ Specify HTTP Method**

```bash
curl -X POST https://example.com/api
```

**Explanation:**

* `-X` specifies the HTTP method: `GET`, `POST`, `PUT`, `DELETE`, etc.
* Default is `GET`.

---

## **4️⃣ Sending Data (POST Request)**

```bash
curl -X POST https://example.com/api -d "username=admin&password=123"
```

**Explanation:**

* `-d` sends **data in POST request body** (`application/x-www-form-urlencoded` by default).
* For JSON data:

```bash
curl -X POST https://example.com/api \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"123"}'
```

---

## **5️⃣ Include Response Headers in Output**

```bash
curl -i https://example.com
```

**Explanation:**

* `-i` prints **HTTP response headers along with the body**.

---

## **6️⃣ Save Output to File**

```bash
curl -o output.html https://example.com
```

**Explanation:**

* `-o` writes the output to a file (`output.html`) instead of printing to terminal.

```bash
curl -O https://example.com/file.zip
```

* `-O` saves the file **with the original name** from the URL.

---

## **7️⃣ Follow Redirects**

```bash
curl -L https://example.com
```

**Explanation:**

* `-L` follows HTTP **redirects** (301, 302) automatically.

---

## **8️⃣ Set Custom Headers**

```bash
curl -H "Authorization: Bearer <token>" -H "Accept: application/json" https://example.com/api
```

**Explanation:**

* `-H` adds **custom HTTP headers**.
* Useful for authentication, API versioning, and content negotiation.

---

## **9️⃣ Authentication**

### **Basic Auth:**

```bash
curl -u username:password https://example.com
```

### **Bearer Token:**

```bash
curl -H "Authorization: Bearer <token>" https://example.com/api
```

---

## **10️⃣ Sending Files**

```bash
curl -F "file=@/path/to/file.txt" https://example.com/upload
```

**Explanation:**

* `-F` sends a **multipart/form-data** POST request.
* `@` symbol indicates file upload.

---

## **11️⃣ Verbose and Debug Mode**

```bash
curl -v https://example.com
```

**Explanation:**

* `-v` prints **detailed request/response info** (useful for debugging).

```bash
curl --trace trace.txt https://example.com
```

* `--trace` logs **all network activity** to a file.

---

## **12️⃣ Limit Download/Upload Speed**

```bash
curl --limit-rate 100k https://example.com/file.zip
```

* Limits transfer speed to **100 KB/s**.
* Useful for testing slow networks or throttling downloads.

---

## **13️⃣ Multiple Requests in One Command**

```bash
curl -O https://example.com/file1.zip -O https://example.com/file2.zip
```

* Downloads multiple files using one command.

---

## **14️⃣ Summary of Common Options**

| Option         | Description                          |
| -------------- | ------------------------------------ |
| `-X`           | HTTP method (GET, POST, PUT, DELETE) |
| `-d`           | POST data (form or JSON)             |
| `-H`           | Custom headers                       |
| `-u`           | Basic authentication                 |
| `-i`           | Include response headers             |
| `-I`           | Fetch headers only                   |
| `-L`           | Follow redirects                     |
| `-o`           | Save output to file                  |
| `-O`           | Save with remote file name           |
| `-v`           | Verbose mode                         |
| `--limit-rate` | Limit transfer speed                 |
| `-F`           | File upload                          |

---

If you want, I can also make a **diagram showing the request/response flow of curl with GET, POST, headers, and authentication**, which makes it very easy to visualize real-world API interactions.

Do you want me to do that?
