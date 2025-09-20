# TCP vs UDP Notes

## 🔹 TCP (Transmission Control Protocol)
- **Connection-oriented** (establishes connection before data transfer).
- **Reliable** → ensures delivery with acknowledgments & retransmissions.
- **Ordered** → packets arrive in sequence.
- **Error checking** → checksum, retransmission if error detected.
- **Slower** due to overhead of reliability.
- **Use cases**: Web browsing (HTTP/HTTPS), Email (SMTP/IMAP/POP3), File transfer (FTP).

---

## 🔹 UDP (User Datagram Protocol)
- **Connectionless** (no handshake, just sends packets).
- **Unreliable** → no guarantee of delivery.
- **No ordering** → packets may arrive out of order or get lost.
- **Minimal error checking** (checksum only).
- **Faster** with low overhead.
- **Use cases**: Video streaming, Online gaming, VoIP, DNS queries.

---

## 🔄 Quick Comparison Table

| Feature            | **TCP**                                | **UDP**                         |
|--------------------|----------------------------------------|---------------------------------|
| Connection         | Connection-oriented                    | Connectionless                   |
| Reliability        | Reliable (ACKs, retransmission)         | Unreliable                       |
| Ordering           | Ensures ordered delivery                | No ordering                      |
| Speed              | Slower (more overhead)                  | Faster (less overhead)           |
| Error Handling     | Strong (retransmission, flow control)   | Basic checksum only              |
| Use Cases          | Web, Email, File Transfer               | Streaming, Gaming, DNS, VoIP     |

---

## 👉 In short
- **TCP = reliable, ordered, slower** → use when accuracy matters.
- **UDP = fast, lightweight, unreliable** → use when speed matters more than accuracy.
