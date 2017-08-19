# Akka Redis Persistent Training

Because I want to know why an error below occurre...

```
EXECABORT Transaction discarded because of previous errors.
```

Database Set Up:
---

```bash
docker run -d -p 3307:3306 \
  --name finagle \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -e MYSQL_USER=finagle \
  -e MYSQL_PASSWORD=finagle \
  -e MYSQL_DATABASE=finagle \
  mysql \
  --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci 
```

```bash
mysql -uroot -p -h127.0.0.1 -P3307 -Dfinagle
```

```bash
sbt flywayMigrate
```
