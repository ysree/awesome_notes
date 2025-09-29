# Apache Kafka Notes

## Table of Contents
- [1. What is Kafka](#1-what-is-kafka)
- [2. Core Concepts](#2-core-concepts)
- [3. Kafka Architecture](#3-kafka-architecture)
- [4. Kafka Features](#4-kafka-features)
- [5. Kafka Data Flow](#5-kafka-data-flow)
- [6. Key Kafka Components](#6-key-kafka-components)
- [7. Reliability & Fault Tolerance](#7-reliability--fault-tolerance)
- [8. Kafka Retention and Compaction](#8-kafka-retention-and-compaction)
- [9. Use Cases](#9-use-cases)
- [10. Kafka Performance Tips](#10-kafka-performance-tips)
- [11. Kafka Security](#11-kafka-security)
- [12. Advanced Features](#12-advanced-features)
- [13. Kafka Best Practices](#13-kafka-best-practices)
- [How Kafka Works](#how-kafka-works)
- [Kafka Deployment as cluster](#kafka-deployment-as-cluster)
- [Kafka Commands Cheat Sheet](#kafka-commands-cheat-sheet)

## 1. What is Kafka

* Apache Kafka is a distributed streaming platform.
* Used for building real-time data pipelines and streaming applications.
* High-throughput, fault-tolerant, scalable, supports publish-subscribe and queue-based messaging.

## 2. Core Concepts

| Term           | Description                                                                                     |
| -------------- | ----------------------------------------------------------------------------------------------- |
| Producer       | Publishes messages to Kafka topics.                                                             |
| Consumer       | Reads messages from Kafka topics.                                                               |
| Topic          | Named stream of messages; can have multiple partitions.                                         |
| Partition      | Log of messages inside a topic; enables parallelism.                                            |
| Broker         | Kafka server storing data and serving clients.                                                  |
| Cluster        | Collection of Kafka brokers working together.                                                   |
| Offset         | Unique identifier for each message in a partition; tracks consumer progress.                    |
| Consumer Group | Group of consumers sharing consumption of messages; enables load balancing.                     |
| Zookeeper      | Manages cluster metadata and leader election (Kafka now supports KRaft mode without Zookeeper). |

## 3. Kafka Architecture

1. Producers publish messages to topics/partitions stored in brokers.
2. Consumers subscribe to topics/partitions and consume messages.
3. Replication ensures fault tolerance; one broker is leader, others followers.
4. Partitioning allows parallel consumption and scalability.
5. Offset management can be automatic or manual.

## 4. Kafka Features

* High throughput (millions of messages/sec).
* Scalability via brokers and partitions.
* Durability: messages persisted on disk with replication.
* Fault tolerance: replicated partitions survive broker failures.
* Real-time streaming.
* Retention policies: messages retained by time or size.

## 5. Kafka Data Flow

1. Producer sends messages to a topic.
2. Kafka assigns messages to a partition using a key or round-robin.
3. Broker writes messages to log files on disk.
4. Consumers fetch messages by offset.
5. Consumer group balances partitions among members.

## 6. Key Kafka Components

* Kafka Broker: Stores messages, serves producers and consumers.
* ZooKeeper / KRaft: Manages cluster metadata and leader election.
* Kafka Connect: ETL framework for integrating Kafka with other systems.
* Kafka Streams: Library for stream processing.
* Schema Registry: Stores Avro/Protobuf schemas for message validation.

## 7. Reliability & Fault Tolerance

* Replication factor: number of copies of each partition.
* Leader-Follower model.
* In-sync replicas (ISR).
* Acknowledgements (acks):

  * acks=0: No wait.
  * acks=1: Wait for leader.
  * acks=all: Wait for all ISR replicas.

## 8. Kafka Retention and Compaction

* Retention by time (e.g., 7 days).
* Retention by size (e.g., 100GB log).
* Log compaction: retain latest message per key.

## 9. Use Cases

* Real-time analytics (clickstreams, telemetry).
* Event sourcing.
* Log aggregation.
* Messaging system (queue, pub/sub).
* ETL pipelines (Kafka Connect).

## 10. Kafka Performance Tips

* Increase partitions for parallelism.
* Optimize producer batch size and linger.ms.
* Tune consumer fetch size and max poll records.
* Monitor ISR and under-replicated partitions.
* Use compression (snappy, gzip).

## 11. Kafka Security

* Authentication: SASL (PLAIN, SCRAM, GSSAPI/Kerberos).
* Authorization: ACLs for topics and consumer groups.
* Encryption: TLS/SSL.

## 12. Advanced Features

* Exactly-once semantics (EOS).
* Kafka Streams for stateful operations.
* KSQL: SQL-like querying on streams.
* MirrorMaker: replicate topics across clusters/regions.

## 13. Kafka Best Practices

* Multiple brokers and partitions for HA.
* Replication factor usually 3.
* Monitor consumer lag.
* Use schema registry for message structure.
* Enable log compaction for critical topics.


# How Kafka Works

## 1. Kafka Components in Action

1. **Producer**: Sends messages (events) to Kafka topics.
2. **Topic**: Logical channel or stream of messages. Topics can be split into **partitions**.
3. **Partition**: Ordered, immutable sequence of messages. Each message has an **offset**.
4. **Broker**: Kafka server that stores partitions and serves clients.
5. **Consumer**: Reads messages from topics, tracking offset to know where it left off.
6. **Consumer Group**: Multiple consumers working together to read a topic. Each partition is consumed by only **one consumer per group**, allowing load balancing.

## 2. Message Flow

1. **Producing Messages**

   * A producer chooses a topic and optionally a **partition key**.
   * Kafka assigns the message to a partition (based on the key or round-robin if no key).
   * The message is appended to the **partition log**, stored durably on disk.
   * The producer can request acknowledgement (`acks`) to ensure reliability:

     * `acks=0`: no confirmation
     * `acks=1`: leader confirms
     * `acks=all`: all replicas confirm

2. **Replication**

   * Each partition has a **leader** and **followers** (replicas).
   * Leader handles reads and writes.
   * Followers replicate data asynchronously for fault tolerance.
   * If a leader fails, a follower from the **in-sync replicas (ISR)** is promoted.

3. **Consuming Messages**

   * Consumer fetches messages from a partition starting at a specific **offset**.
   * Consumers in a **consumer group** share partitions for parallel processing.
   * Offsets can be committed automatically or manually to track progress.
   * This allows **replay of messages** if needed.

4. **Retention and Log Management**

   * Kafka keeps messages based on **time** (e.g., 7 days) or **size** (e.g., 100 GB per partition).
   * Optional **log compaction** retains the latest message per key for stateful use cases.

## 3. Data Durability and Fault Tolerance

* Messages are written to disk (persisted) for durability.
* Partition replication ensures data survives broker failure.
* ISR guarantees consistency among replicas.
* Consumers can re-read messages using offsets, supporting **event replay**.

## 4. Scaling and Parallelism

* Add more **partitions** to a topic → increases parallelism.
* Add more **brokers** → distributes partitions across cluster for load balancing.
* Consumer groups can scale horizontally to read data in parallel without duplication.

## 5. Summary Flow Diagram (Text Version)

```
Producer --> Topic --> Partition --> Broker (Leader + Followers)
                     |--> Consumer Group --> Consumer(s)
                     |--> Replication --> Follower Broker(s)
```

* Producer writes messages → Kafka stores them in partitions.
* Broker handles replication and ensures durability.
* Consumers read messages from partitions → can scale horizontally.
* Offset tracking allows **replay and fault-tolerant processing**.

Kafka works by combining **distributed log storage, replication, and consumer groups** to provide a **reliable, fault-tolerant, and high-throughput streaming platform**.

# Kafka Deployment as cluster
# Deploy Apache Kafka KRaft Cluster on Kubernetes

## 1. Prerequisites

* Kubernetes cluster (v1.24+ recommended).
* `kubectl` configured.
* Persistent storage available (StorageClass).
* Minimum 3 Kafka brokers recommended for a KRaft cluster.

---

## 2. Create Namespace

```bash
kubectl create namespace kafka
```

---

## 3. Kafka KRaft Configuration (ConfigMap)

`kafka-config.yaml`

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: kafka-config
  namespace: kafka
data:
  server.properties: |
    broker.id=1
    process.roles=broker,controller
    node.id=1
    controller.quorum.voters=1@kafka-0.kafka-headless.kafka.svc.cluster.local:9093,2@kafka-1.kafka-headless.kafka.svc.cluster.local:9093,3@kafka-2.kafka-headless.kafka.svc.cluster.local:9093
    listeners=PLAINTEXT://:9092,CONTROLLER://:9093
    listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
    inter.broker.listener.name=PLAINTEXT
    log.dirs=/var/lib/kafka/data
    num.partitions=3
    default.replication.factor=3
    offsets.topic.replication.factor=3
    transaction.state.log.replication.factor=3
    transaction.state.log.min.isr=2
```

---

## 4. Headless Service

`kafka-headless-service.yaml`

```yaml
apiVersion: v1
kind: Service
metadata:
  name: kafka-headless
  namespace: kafka
spec:
  clusterIP: None
  selector:
    app: kafka
  ports:
    - port: 9092
      name: broker
    - port: 9093
      name: controller
```

---

## 5. Kafka StatefulSet

`kafka-statefulset.yaml`

```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: kafka
  namespace: kafka
spec:
  serviceName: kafka-headless
  replicas: 3
  selector:
    matchLabels:
      app: kafka
  template:
    metadata:
      labels:
        app: kafka
    spec:
      containers:
        - name: kafka
          image: apache/kafka:3.5.0
          ports:
            - containerPort: 9092
              name: broker
            - containerPort: 9093
              name: controller
          env:
            - name: KAFKA_KRAFT_MODE
              value: "true"
            - name: KAFKA_BROKER_ID
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: KAFKA_CFG_LISTENERS
              value: PLAINTEXT://:9092,CONTROLLER://:9093
            - name: KAFKA_CFG_CONTROLLER_QUORUM_VOTERS
              value: "1@kafka-0.kafka-headless.kafka.svc.cluster.local:9093,2@kafka-1.kafka-headless.kafka.svc.cluster.local:9093,3@kafka-2.kafka-headless.kafka.svc.cluster.local:9093"
          volumeMounts:
            - name: kafka-data
              mountPath: /var/lib/kafka/data
  volumeClaimTemplates:
    - metadata:
        name: kafka-data
      spec:
        accessModes: ["ReadWriteOnce"]
        resources:
          requests:
            storage: 10Gi
```

---

## 6. Deploy Kafka Cluster

```bash
kubectl apply -f kafka-config.yaml
kubectl apply -f kafka-headless-service.yaml
kubectl apply -f kafka-statefulset.yaml
```

Check pods:

```bash
kubectl get pods -n kafka
```

---

## 7. Access Kafka

Internal PLAINTEXT port: `9092`

```bash
kubectl exec -it kafka-0 -n kafka -- /bin/bash
kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.kafka.svc.cluster.local:9092 --create --topic test-topic --partitions 3 --replication-factor 3
kafka-topics.sh --bootstrap-server kafka-0.kafka-headless.kafka.svc.cluster.local:9092 --list
```

---

## 8. Notes

* KRaft mode eliminates Zookeeper; metadata is managed by Kafka brokers.
* StatefulSet ensures stable network identities and persistent storage.
* For production, configure external access via NodePort, LoadBalancer, or Ingress.
* Odd number of brokers recommended for KRaft quorum.

# Kafka commands
# Kafka Commands Cheat Sheet

## 1. Create a Topic

```bash
# Create a topic with 3 partitions and replication factor of 3
kafka-topics.sh --bootstrap-server <BROKER_ADDRESS>:9092 \
                --create \
                --topic my-topic \
                --partitions 3 \
                --replication-factor 3

# List all topics
kafka-topics.sh --bootstrap-server <BROKER_ADDRESS>:9092 --list

# Describe a topic
kafka-topics.sh --bootstrap-server <BROKER_ADDRESS>:9092 --topic my-topic --describe
```

---

## 2. Delete a Topic

```bash
kafka-topics.sh --bootstrap-server <BROKER_ADDRESS>:9092 --topic my-topic --delete
```

---

## 3. Produce Messages

```bash
# Start console producer
kafka-console-producer.sh --broker-list <BROKER_ADDRESS>:9092 --topic my-topic

# Type messages and press Enter to send
Hello Kafka
This is a test message
```

**Optional: Produce messages from a file**

```bash
kafka-console-producer.sh --broker-list <BROKER_ADDRESS>:9092 --topic my-topic < messages.txt
```

---

## 4. Consume Messages

```bash
# Start console consumer from beginning
kafka-console-consumer.sh --bootstrap-server <BROKER_ADDRESS>:9092 \
                           --topic my-topic \
                           --from-beginning

# Start consumer for only new messages
kafka-console-consumer.sh --bootstrap-server <BROKER_ADDRESS>:9092 --topic my-topic
```

---

## 5. Work with Consumer Groups

```bash
# Consume messages as part of a group
kafka-console-consumer.sh --bootstrap-server <BROKER_ADDRESS>:9092 \
                           --topic my-topic \
                           --group my-group \
                           --from-beginning

# List all consumer groups
kafka-consumer-groups.sh --bootstrap-server <BROKER_ADDRESS>:9092 --list

# Describe a consumer group
kafka-consumer-groups.sh --bootstrap-server <BROKER_ADDRESS>:9092 --group my-group --describe

# Reset consumer group offset
kafka-consumer-groups.sh --bootstrap-server <BROKER_ADDRESS>:9092 \
                          --group my-group \
                          --reset-offsets \
                          --to-earliest \
                          --execute \
                          --topic my-topic
```

---

## 6. Produce/Consume with KRaft Kafka Cluster

* Use the **bootstrap server address of any broker**, e.g., `kafka-0.kafka-headless.kafka.svc.cluster.local:9092`.
* All the commands above work the same as with Zookeeper-based clusters.

