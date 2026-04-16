# 🚀 LMS Backend Deployment Guide

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Build](#build)
3. [Local Development](#local-development)
4. [Production Deployment](#production-deployment)
5. [Troubleshooting](#troubleshooting)
6. [Monitoring](#monitoring)

---

## Prerequisites

### System Requirements
- **OS**: Linux, macOS, or Windows
- **Java**: JDK 17 or higher
- **Maven**: 3.6.0 or higher
- **MySQL**: 8.0 or higher
- **RAM**: Minimum 2GB (4GB recommended)
- **Disk**: Minimum 500MB free space

### Installation Commands

#### macOS (using Homebrew)
```bash
# Install Java 17
brew install openjdk@17

# Install Maven
brew install maven

# Install MySQL
brew install mysql
brew services start mysql
```

#### Ubuntu/Debian
```bash
# Install Java 17
sudo apt-get update
sudo apt-get install openjdk-17-jdk

# Install Maven
sudo apt-get install maven

# Install MySQL
sudo apt-get install mysql-server
sudo mysql_secure_installation
sudo systemctl start mysql
```

#### Windows (using Chocolatey)
```powershell
# Install Java 17
choco install openjdk17

# Install Maven
choco install maven

# Install MySQL
choco install mysql
```

---

## Build

### Step 1: Navigate to Project Directory
```bash
cd /Users/ilkinismayilov/Downloads/LMS
```

### Step 2: Build the Project
```bash
# Clean and compile
mvn clean compile

# Compile and package
mvn clean package
```

### Step 3: Verify Build
```bash
# Check if JAR file exists
ls -la target/LMS-0.0.1-SNAPSHOT.jar

# Output should show:
# -rw-r--r--  1 user  group  X.XMB  Apr 16 timestamp LMS-0.0.1-SNAPSHOT.jar
```

---

## Local Development

### Step 1: Setup MySQL Database

```bash
# Start MySQL
mysql -u root -p

# Create database
CREATE DATABASE llm;
CREATE USER 'lms_user'@'localhost' IDENTIFIED BY 'LmsPassword123!';
GRANT ALL PRIVILEGES ON llm.* TO 'lms_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Step 2: Update Configuration

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/llm?useSSL=false&serverTimezone=UTC
    username: lms_user
    password: LmsPassword123!
```

### Step 3: Run Application

```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Using Java directly (after building)
java -jar target/LMS-0.0.1-SNAPSHOT.jar

# Application will start on http://localhost:8080
```

### Step 4: Verify Application

```bash
# Check if application is running
curl http://localhost:8080/swagger-ui.html

# Or open in browser
# http://localhost:8080/swagger-ui.html
```

---

## Production Deployment

### Step 1: Build Release JAR

```bash
# Create optimized production build
mvn clean package -DskipTests -Dmaven.compiler.optimize=true
```

### Step 2: Create Environment Configuration

Create `application-prod.yaml`:

```yaml
spring:
  application:
    name: LMS
  datasource:
    url: jdbc:mysql://db-server.example.com:3306/lms_prod
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    hibernate:
      ddl-auto: validate  # Use 'validate' for production
    show-sql: false
  web:
    cors:
      allowed-origins: https://your-domain.com

app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 86400000

logging:
  level:
    root: WARN
    com.example: INFO
  file:
    name: /var/log/lms/application.log
  logback:
    rollingpolicy:
      max-file-size: 10MB
      max-history: 30
```

### Step 3: Deploy to Server

#### Option 1: Using Docker

Create `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR
COPY target/LMS-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

Build and push:

```bash
# Build Docker image
docker build -t lms-backend:latest .

# Tag for registry
docker tag lms-backend:latest registry.example.com/lms-backend:latest

# Push to registry
docker push registry.example.com/lms-backend:latest

# Run container
docker run -d \
  -e DB_USER=lms_user \
  -e DB_PASSWORD=${DB_PASSWORD} \
  -e JWT_SECRET=${JWT_SECRET} \
  -p 8080:8080 \
  --name lms-backend \
  registry.example.com/lms-backend:latest
```

#### Option 2: Direct Server Deployment

```bash
# SSH into server
ssh user@your-server.com

# Create application directory
sudo mkdir -p /opt/lms
sudo chown -R $USER:$USER /opt/lms

# Upload JAR
scp target/LMS-0.0.1-SNAPSHOT.jar user@your-server.com:/opt/lms/

# Create systemd service
sudo nano /etc/systemd/system/lms.service
```

Systemd service file content:

```ini
[Unit]
Description=LMS Backend Application
After=network.target

[Service]
Type=simple
User=lms
WorkingDirectory=/opt/lms
Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="DB_USER=lms_user"
Environment="DB_PASSWORD=SecurePassword123!"
Environment="JWT_SECRET=YourSuperSecretKeyThatIsAtLeast256BitsLongForHS256"
ExecStart=/usr/bin/java -jar /opt/lms/LMS-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

Start service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable lms
sudo systemctl start lms
sudo systemctl status lms
```

### Step 4: Configure Reverse Proxy (Nginx)

```nginx
upstream lms_backend {
    server localhost:8080;
}

server {
    listen 443 ssl http2;
    server_name api.your-domain.com;

    ssl_certificate /etc/letsencrypt/live/api.your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.your-domain.com/privkey.pem;

    client_max_body_size 10M;

    location / {
        proxy_pass http://lms_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # CORS headers
        add_header 'Access-Control-Allow-Origin' '*' always;
        add_header 'Access-Control-Allow-Methods' 'GET, POST, PUT, DELETE, OPTIONS' always;
        add_header 'Access-Control-Allow-Headers' 'Content-Type, Authorization' always;
    }
}

# Redirect HTTP to HTTPS
server {
    listen 80;
    server_name api.your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

Reload Nginx:

```bash
sudo nginx -t
sudo systemctl reload nginx
```

---

## Troubleshooting

### Build Issues

#### Issue: "Java version not compatible"
```bash
# Check Java version
java -version

# Must be 17 or higher
```

#### Issue: Maven compilation fails
```bash
# Clear Maven cache
mvn clean

# Update dependencies
mvn -U clean compile
```

### Runtime Issues

#### Issue: Database connection refused
```bash
# Check MySQL status
sudo systemctl status mysql

# Verify database credentials
mysql -u lms_user -p -h localhost llm

# Check application logs
tail -f /var/log/lms/application.log
```

#### Issue: Port 8080 already in use
```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>

# Or use different port
java -jar app.jar --server.port=8081
```

#### Issue: JWT token invalid
```bash
# Verify JWT_SECRET environment variable
echo $JWT_SECRET

# Token must match secret used during generation
```

---

## Monitoring

### Application Logs

```bash
# Real-time logs
tail -f /var/log/lms/application.log

# Last 100 lines
tail -n 100 /var/log/lms/application.log

# Filter by log level
grep "ERROR" /var/log/lms/application.log
grep "WARN" /var/log/lms/application.log
```

### Health Check

```bash
# Check application health
curl http://localhost:8080/api/v1/auth/login

# Expected response: 200 OK or 401 Unauthorized
# If 502 Bad Gateway, application is not running
```

### Database Monitoring

```bash
# Check database connection pool
# Add to application.yaml:
# spring.jpa.properties.hibernate.generate_statistics: true

# Monitor queries
mysql -u lms_user -p
SHOW PROCESSLIST;
SHOW STATUS;
```

### Performance Monitoring

```bash
# Monitor CPU and memory
top

# Monitor disk space
df -h

# Monitor network connections
netstat -tlnp | grep java
```

---

## Backup & Recovery

### Database Backup

```bash
# Full backup
mysqldump -u lms_user -p llm > llm_backup_$(date +%Y%m%d_%H%M%S).sql

# Scheduled backup
0 2 * * * mysqldump -u lms_user -p llm > /backups/llm_$(date +\%Y\%m\%d).sql

# Restore from backup
mysql -u lms_user -p llm < llm_backup_20260416_120000.sql
```

### Application Backup

```bash
# Backup JAR and configuration
tar -czf lms_backup_$(date +%Y%m%d).tar.gz /opt/lms/ /etc/systemd/system/lms.service
```

---

## Security Checklist

- [ ] Change default JWT secret
- [ ] Update database passwords
- [ ] Enable HTTPS/SSL
- [ ] Configure firewall rules
- [ ] Set up log rotation
- [ ] Enable database backups
- [ ] Configure CORS correctly
- [ ] Use environment variables for secrets
- [ ] Enable MySQL password authentication
- [ ] Set file permissions properly (chmod 600 for config)
- [ ] Regular security updates
- [ ] Monitor access logs

---

## Performance Tuning

### MySQL Configuration

```ini
# /etc/mysql/mysql.conf.d/mysqld.cnf

[mysqld]
# Increase connection pool
max_connections = 100

# Increase buffer pool (for large datasets)
innodb_buffer_pool_size = 2G

# Enable slow query log
slow_query_log = 1
long_query_time = 2
```

### Application Configuration

```yaml
spring:
  jpa:
    properties:
      hibernate:
        # Use batch inserts
        jdbc.batch_size: 20
        # Generate statistics for monitoring
        generate_statistics: true
```

---

## Support & Resources

### Logs Location
- Application logs: `/var/log/lms/application.log`
- System logs: `/var/log/syslog`
- MySQL logs: `/var/log/mysql/error.log`

### Useful Commands
```bash
# Check Java process
ps aux | grep java

# Check port status
netstat -tlnp

# Monitor application in real-time
jps -l

# Heap memory usage
jstat -gc <pid> 1000
```

### Documentation
- API Docs: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs
- GitHub: [Your Repository]

---

**Last Updated**: April 16, 2026  
**Version**: 1.0.0  
**Status**: Production Ready

