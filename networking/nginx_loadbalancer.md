
## 1. Basic Load Balancing Setup

### Upstream Block Configuration

```nginx
http {
    upstream backend_servers {
        # Define backend servers
        server 192.168.1.10:80;
        server 192.168.1.11:80;
        server 192.168.1.12:80;
    }

    server {
        listen 80;
        server_name example.com;

        location / {
            # Pass requests to the upstream group
            proxy_pass http://backend_servers;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }
}
```

## 2. Load Balancing Methods

### Round Robin (Default)
```nginx
upstream backend {
    # Round Robin is default - requests distributed sequentially
    server 192.168.1.10:80;
    server 192.168.1.11:80;
    server 192.168.1.12:80;
}
```

### Least Connections
```nginx
upstream backend {
    least_conn;  # Sends request to server with fewest active connections
    server 192.168.1.10:80;
    server 192.168.1.11:80;
    server 192.168.1.12:80;
}
```

### IP Hash
```nginx
upstream backend {
    ip_hash;  # Client IP determines server - session persistence
    server 192.168.1.10:80;
    server 192.168.1.11:80;
    server 192.168.1.12:80;
}
```

### Generic Hash
```nginx
upstream backend {
    hash $request_uri consistent;  # Distributes based on request URI
    server 192.168.1.10:80;
    server 192.168.1.11:80;
    server 192.168.1.12:80;
}
```

## 3. Weighted Distribution

```nginx
upstream backend {
    server 192.168.1.10:80 weight=3;  # 3/6 = 50% of traffic
    server 192.168.1.11:80 weight=2;  # 2/6 = 33% of traffic
    server 192.168.1.12:80 weight=1;  # 1/6 = 17% of traffic
}
```

## 4. Server Health Checks and Backup

```nginx
upstream backend {
    server 192.168.1.10:80 max_fails=3 fail_timeout=30s;
    server 192.168.1.11:80 max_fails=3 fail_timeout=30s;
    server 192.168.1.12:80 backup;  # Used only when primary servers are down
    
    # Health check settings
    health_check interval=10 fails=3 passes=2;
}
```

## 5. Advanced Traffic Distribution Scenarios

### Geographic Distribution
```nginx
# Based on client region or specific paths
upstream us_servers {
    server 10.1.1.10:80;
    server 10.1.1.11:80;
}

upstream eu_servers {
    server 10.2.1.10:80;
    server 10.2.1.11:80;
}

# Map based on geographic data or custom headers
geo $user_region {
    default        us_servers;
    US             us_servers;
    EU             eu_servers;
}

server {
    listen 80;
    
    location / {
        proxy_pass http://$user_region;
    }
}
```

### Application-Based Routing
```nginx
upstream api_servers {
    server 192.168.1.20:8080;
    server 192.168.1.21:8080;
}

upstream web_servers {
    server 192.168.1.30:80;
    server 192.168.1.31:80;
}

upstream static_servers {
    server 192.168.1.40:80;
}

server {
    listen 80;
    
    location /api/ {
        proxy_pass http://api_servers;
    }
    
    location /static/ {
        proxy_pass http://static_servers;
    }
    
    location / {
        proxy_pass http://web_servers;
    }
}
```

## 6. Complete Example with All Features

```nginx
http {
    upstream production_backend {
        # Weighted least connections with health checks
        least_conn;
        
        server 192.168.1.10:80 weight=5 max_fails=3 fail_timeout=30s;
        server 192.168.1.11:80 weight=3 max_fails=3 fail_timeout=30s;
        server 192.168.1.12:80 weight=2 max_fails=3 fail_timeout=30s;
        server 192.168.1.13:80 backup;  # Backup server
        
        # Session persistence (sticky sessions)
        sticky cookie srv_id expires=1h domain=.example.com path=/;
    }

    upstream staging_backend {
        server 192.168.2.10:80;
        server 192.168.2.11:80;
    }

    # Split traffic between production and staging
    split_clients "${remote_addr}AAA" $version {
        95%    production_backend;
        5%     staging_backend;
    }

    server {
        listen 80;
        server_name app.example.com;

        # Enhanced proxy settings
        location / {
            proxy_pass http://$version;
            
            # Proxy headers
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            
            # Timeout settings
            proxy_connect_timeout 30s;
            proxy_send_timeout 30s;
            proxy_read_timeout 30s;
            
            # Buffer settings
            proxy_buffering on;
            proxy_buffer_size 4k;
            proxy_buffers 8 4k;
            
            # Error handling
            proxy_next_upstream error timeout invalid_header http_500 http_502 http_503 http_504;
            proxy_next_upstream_tries 3;
        }

        # Health check endpoint
        location /nginx_status {
            stub_status on;
            access_log off;
            allow 192.168.1.0/24;
            deny all;
        }
    }
}
```

## 7. Monitoring and Statistics

### Enable Status Page
```nginx
server {
    listen 8080;
    server_name localhost;
    
    location /nginx_status {
        stub_status on;
        access_log off;
        allow 127.0.0.1;
        deny all;
    }
    
    location /upstream_status {
        upstream_status;
        access_log off;
        allow 127.0.0.1;
        deny all;
    }
}
```

## 8. Configure NGINX for SSL

Edit your site config (e.g. `/etc/nginx/sites-available/yourdomain.com`):

```
server {
    listen 443 ssl;
    server_name yourdomain.com www.yourdomain.com;

    ssl_certificate     /etc/ssl/certs/yourdomain.crt;
    ssl_certificate_key /etc/ssl/private/yourdomain.key;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```


## Key Configuration Directives Explained:

- **weight**: Controls traffic distribution ratio
- **max_fails**: Number of failed attempts before marking server unavailable
- **fail_timeout**: Time period after which failed server is retried
- **backup**: Server used only when all primary servers are down
- **down**: Manually marks server as permanently unavailable
- **least_conn**: Distributes to server with fewest active connections
- **ip_hash**: Ensures client sticks to same server (session persistence)
- **health_check**: Active health monitoring

## Best Practices:

1. **Always configure health checks** to automatically remove unhealthy servers
2. **Use appropriate timeouts** based on your application requirements
3. **Implement proper logging** for troubleshooting traffic distribution
4. **Monitor upstream servers** using the status page
5. **Test failover scenarios** to ensure high availability
6. **Use weighted distribution** for servers with different capacities

This configuration provides a robust foundation for traffic distribution in Nginx with various load balancing strategies to suit different application requirements.