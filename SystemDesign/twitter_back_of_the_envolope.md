## Assumptions:

- Total Users: 1,000,000
- User Breakdown: 30% active (300K), 50% medium active (500K), 20% non-active (200K)
- Text: 1 KB/message
- Images: 2 MB/image (compressed, e.g., JPEG)
- Videos: 50 MB/video (1-minute, 1080p, compressed)
- Actions per day:

- Active: 50 texts, 5 images, 0.2 videos (60,000 videos total)
- Medium: 10 texts, 1 image, 0.02 videos
- Non-Active: 1 text, 0.1 images, 0.002 videos


**Replication: 3x**

# Calculations:
## Text:

#### Active: 
300K × 50 × 1 KB = 15,000,000 KB/day = 15 GB/day

Yearly: 15 GB/day × 365 = 5,475 GB/year = 5.475 TB/year
Replicated: 5.475 TB/year × 3 = 16.425 TB/year


#### Medium: 
500K × 10 × 1 KB = 5,000,000 KB/day = 5 GB/day

Yearly: 5 GB/day × 365 = 1,825 GB/year = 1.825 TB/year
Replicated: 1.825 TB/year × 3 = 5.475 TB/year


#### Non-Active: 
200K × 1 × 1 KB = 200,000 KB/day = 0.2 GB/day

Yearly: 0.2 GB/day × 365 = 73 GB/year = 0.073 TB/year
Replicated: 0.073 TB/year × 3 = 0.219 TB/year


**Total Text:**

Daily: 15 GB + 5 GB + 0.2 GB = 20.2 GB/day
Yearly: 5.475 TB + 1.825 TB + 0.073 TB = 7.373 TB/year
Replicated: 16.425 TB + 5.475 TB + 0.219 TB = 22.119 TB/year



## Images:

#### Active: 
300K × 5 × 2 MB = 3,000,000 MB/day = 3,000 GB/day = 3 TB/day

Yearly: 3 TB/day × 365 = 1,095 TB/year
Replicated: 1,095 TB/year × 3 = 3,285 TB/year


#### Medium: 
500K × 1 × 2 MB = 1,000,000 MB/day = 1,000 GB/day = 1 TB/day

Yearly: 1 TB/day × 365 = 365 TB/year
Replicated: 365 TB/year × 3 = 1,095 TB/year


#### Non-Active: 
200K × 0.1 × 2 MB = 40,000 MB/day = 40 GB/day = 0.04 TB/day

Yearly: 0.04 TB/day × 365 = 14.6 TB/year
Replicated: 14.6 TB/year × 3 = 43.8 TB/year


**Total Images:**

Daily: 3 TB + 1 TB + 0.04 TB = 4.04 TB/day
Yearly: 1,095 TB + 365 TB + 14.6 TB = 1,474.6 TB/year
Replicated: 3,285 TB + 1,095 TB + 43.8 TB = 4,423.8 TB/year



## Videos:

#### Active: 
300K × 0.2 × 50 MB = 3,000,000 MB/day = 3,000 GB/day = 3 TB/day

Yearly: 3 TB/day × 365 = 1,095 TB/year
Replicated: 1,095 TB/year × 3 = 3,285 TB/year


#### Medium: 
500K × 0.02 × 50 MB = 500,000 MB/day = 500 GB/day = 0.5 TB/day

Yearly: 0.5 TB/day × 365 = 182.5 TB/year
Replicated: 182.5 TB/year × 3 = 547.5 TB/year


#### Non-Active: 
200K × 0.002 × 50 MB = 20,000 MB/day = 20 GB/day = 0.02 TB/day

Yearly: 0.02 TB/day × 365 = 7.3 TB/year
Replicated: 7.3 TB/year × 3 = 21.9 TB/year


**Total Videos:**

- Daily: 3 TB + 0.5 TB + 0.02 TB = 3.52 TB/day
- Yearly: 1,095 TB + 182.5 TB + 7.3 TB = 1,284.8 TB/year
- Replicated: 3,285 TB + 547.5 TB + 21.9 TB = 3,854.4 TB/year



## Overall Total:

### Daily:

- Text: 20.2 GB = 0.0202 TB
- Images: 4.04 TB
- Videos: 3.52 TB
- Total: 0.0202 TB + 4.04 TB + 3.52 TB = 7.5802 TB/day


### Yearly:

- Text: 7.373 TB
- Images: 1,474.6 TB
- Videos: 1,284.8 TB
- Total: 7.373 TB + 1,474.6 TB + 1,284.8 TB = 2,766.773 TB/year


### Replicated Yearly 3 servers:

- Text: 22.119 TB
- Images: 4,423.8 TB
- Videos: 3,854.4 TB
- Total: 22.119 TB + 4,423.8 TB + 3,854.4 TB = 8,300.319 TB/year


### 5-Year Total (Replicated):

- 8,300.319 TB/year × 5 = 41,501.595 TB = ~41,502 TB
- Replicated (3x already included): ~41,502 TB = ~41.5 PB



Optimized 5-Year Total (with -50% reduction):

New Total: 41.5 PB × 0.5 = ~20.75 PB