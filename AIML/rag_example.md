Got it! I'll provide you a **full working code repository structure** with all key files, each with well-commented code explaining every step.

Here’s what the repo will contain:

```
private-ai-rag-bot/
├── ingest_embed.py           # Ingest and embed docs, save embeddings
├── faiss_indexer.py          # Build and save FAISS index
├── rag_backend.py            # Flask backend to handle queries and generate answers
├── llm_server.py             # Simple LLM inference microservice (GPT-2 example)
├── requirements.txt          # Python dependencies
├── Dockerfile.backend        # Dockerfile for rag_backend + faiss index
├── Dockerfile.llm            # Dockerfile for llm_server microservice
├── sample_docs/              # Folder with sample text docs
│   ├── doc1.txt
│   └── doc2.txt
└── README.md                 # Project explanation and setup guide
```

---

### 1. `ingest_embed.py`

This script reads documents, chunks them, embeds, and saves data.

```python
# ingest_embed.py
"""
Reads text documents, splits into smaller chunks,
embeds chunks using Sentence Transformers, saves chunks and embeddings.
"""

import os
import nltk
from sentence_transformers import SentenceTransformer
import numpy as np

# Download punkt tokenizer for sentence splitting
nltk.download('punkt')

model = SentenceTransformer('all-MiniLM-L6-v2')  # Lightweight embedding model

def chunk_text(text, max_words=100):
    """
    Split text into chunks of ~max_words tokens.
    """
    sentences = nltk.sent_tokenize(text)
    chunks = []
    chunk = []
    count = 0
    for sent in sentences:
        words = sent.split()
        if count + len(words) > max_words:
            # Save current chunk and reset
            chunks.append(' '.join(chunk))
            chunk = []
            count = 0
        chunk.append(sent)
        count += len(words)
    if chunk:
        chunks.append(' '.join(chunk))
    return chunks

def ingest_documents(folder_path):
    """
    Read all .txt files in folder and chunk them.
    Returns list of text chunks.
    """
    docs = []
    for filename in os.listdir(folder_path):
        if filename.endswith('.txt'):
            filepath = os.path.join(folder_path, filename)
            with open(filepath, 'r', encoding='utf-8') as f:
                text = f.read()
                chunks = chunk_text(text)
                docs.extend(chunks)
    return docs

def embed_and_save(docs):
    """
    Embed text chunks and save chunks + embeddings to disk.
    """
    print(f"Embedding {len(docs)} chunks...")
    embeddings = model.encode(docs, show_progress_bar=True, convert_to_numpy=True)
    np.save('doc_chunks.npy', np.array(docs))
    np.save('doc_embeddings.npy', embeddings)
    print("Saved doc_chunks.npy and doc_embeddings.npy")

if __name__ == '__main__':
    docs = ingest_documents('./sample_docs')  # Change folder as needed
    embed_and_save(docs)
```

---

### 2. `faiss_indexer.py`

Builds and saves FAISS vector index.

```python
# faiss_indexer.py
"""
Loads embeddings and builds a FAISS index.
Saves the index to disk for retrieval later.
"""

import faiss
import numpy as np

def build_and_save_index(embedding_file='doc_embeddings.npy', index_file='faiss_index.bin'):
    # Load embeddings from file
    embeddings = np.load(embedding_file)
    dim = embeddings.shape[1]

    # Build FAISS index (Flat L2 for simplicity)
    index = faiss.IndexFlatL2(dim)
    index.add(embeddings)

    # Save index
    faiss.write_index(index, index_file)
    print(f"FAISS index saved to {index_file}")

if __name__ == '__main__':
    build_and_save_index()
```

---

### 3. `rag_backend.py`

Flask app for query handling, retrieval, prompt creation, and LLM call.

```python
# rag_backend.py
"""
Flask backend that:
- Loads FAISS index and documents
- Accepts user queries
- Retrieves top relevant chunks
- Constructs prompt and calls LLM microservice
- Returns generated answer
"""

from flask import Flask, request, jsonify
import numpy as np
import faiss
from sentence_transformers import SentenceTransformer
import requests

app = Flask(__name__)

# Load doc chunks and FAISS index on startup
doc_chunks = np.load('doc_chunks.npy', allow_pickle=True)
index = faiss.read_index('faiss_index.bin')
embed_model = SentenceTransformer('all-MiniLM-L6-v2')

# Endpoint for LLM inference service
LLM_ENDPOINT = "http://llm-server:8000/generate"  # Change if needed

def retrieve_relevant_docs(query, top_k=5):
    """
    Embed query, search FAISS index for top_k chunks.
    Returns list of text chunks.
    """
    query_embedding = embed_model.encode([query])
    distances, indices = index.search(query_embedding, top_k)
    return [doc_chunks[i] for i in indices[0]]

def build_prompt(question, chunks):
    """
    Combine retrieved chunks and question into a prompt.
    """
    context = "\n\n".join(chunks)
    prompt = f"Use the following context to answer the question:\n{context}\n\nQuestion: {question}\nAnswer:"
    return prompt

@app.route('/ask', methods=['POST'])
def ask():
    """
    POST endpoint to accept JSON {"question": "..."}
    Returns JSON {"answer": "..."}
    """
    data = request.json
    question = data.get('question', '')
    if not question:
        return jsonify({'error': 'No question provided'}), 400

    # Retrieve relevant chunks from vector DB
    chunks = retrieve_relevant_docs(question)
    prompt = build_prompt(question, chunks)

    # Call LLM inference microservice
    response = requests.post(LLM_ENDPOINT, json={"prompt": prompt, "max_tokens": 256})
    if response.status_code != 200:
        return jsonify({'error': 'LLM service error'}), 500

    answer = response.json().get('text', '')
    return jsonify({'answer': answer})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
```

---

### 4. `llm_server.py`

Simple GPT-2 based LLM inference microservice.

```python
# llm_server.py
"""
Flask microservice hosting LLM inference API.
Uses HuggingFace GPT-2 model for generation.
"""

from flask import Flask, request, jsonify
from transformers import pipeline, set_seed

app = Flask(__name__)

# Load GPT-2 text generation pipeline
generator = pipeline('text-generation', model='gpt2')
set_seed(42)

@app.route('/generate', methods=['POST'])
def generate():
    data = request.json
    prompt = data.get('prompt', '')
    max_tokens = data.get('max_tokens', 100)

    # Generate text from prompt
    outputs = generator(prompt, max_length=max_tokens, num_return_sequences=1)
    generated_text = outputs[0]['generated_text']

    return jsonify({'text': generated_text})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)
```

---

### 5. `requirements.txt`

```
flask
sentence-transformers
nltk
faiss-cpu
transformers
torch
requests
```

---

### 6. `Dockerfile.backend`

```dockerfile
# Dockerfile.backend
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY ingest_embed.py .
COPY faiss_indexer.py .
COPY rag_backend.py .
COPY doc_chunks.npy .
COPY doc_embeddings.npy .
COPY faiss_index.bin .

EXPOSE 5000

CMD ["python", "rag_backend.py"]
```

---

### 7. `Dockerfile.llm`

```dockerfile
# Dockerfile.llm
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY llm_server.py .

EXPOSE 8000

CMD ["python", "llm_server.py"]
```

---

### 8. `sample_docs/doc1.txt`

```
Our company supports flexible remote work policies to enable work-life balance.
Employees can work remotely up to three days a week.
```

---

### 9. `README.md`

````markdown
# Private AI RAG Chatbot Example

This repo demonstrates building a Retrieval-Augmented Generation (RAG) chatbot on private documents using:

- Sentence Transformers for embeddings
- FAISS for vector database
- Flask backend for retrieval + prompt construction + LLM call
- Simple LLM microservice using GPT-2 (HuggingFace)

## Setup and Run

1. Put your private `.txt` docs into `sample_docs/`

2. Ingest and embed docs:

```bash
python ingest_embed.py
````

3. Build FAISS index:

```bash
python faiss_indexer.py
```

4. Build and run LLM microservice:

```bash
docker build -f Dockerfile.llm -t llm-server .
docker run -p 8000:8000 llm-server
```

5. Build and run RAG backend:

```bash
docker build -f Dockerfile.backend -t rag-backend .
docker run -p 5000:5000 rag-backend
```

6. Test chatbot API:

```bash
curl -X POST http://localhost:5000/ask -H "Content-Type: application/json" -d '{"question":"What is the remote work policy?"}'
```

## Notes

* Replace GPT-2 with your private or larger LLM model.
* Replace FAISS with PGVector for production-grade vector DB.
* Add frontend as needed for chat UI.

```

---

---

If you want, I can also generate a **simple React frontend** next for user-friendly chatting!

Would you like me to prepare that?
```
