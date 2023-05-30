# blog-backend

Blog backend in Java Quarkus für das HFTM Modul IN306 Verteilte Systeme.

# Starten und Testen

Projekt kann im Verzeichnis blog-backend  gestartet werden mit dem Befehl:

```
./mvnw quarkus:dev
```

Auf der Quarkus-Konsole kann mit *r* das Testing gestartet werden.

*Wichtig um mit podman betrieben zu werden muss die DOCKER_HOST env gesetzt werden*

Siehe [guide quarkus with podman](https://quarkus.io/guides/podman).

```
export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock
```

## Erstellen eines MariaDB containers auf Podman

Erstellen einet mariadb containers.

```
podman run -d --name=backend-mariadb -p 9001:3306 -e MARIADB_USER=backend -e MARIADB_PASSWORD=<YourPassHere> -e MARIADB_ROOT_PASSWORD=<YourPassHere> mariadb:latest
```

Erstellen der Datenbank und vergeben der Rechte auf der podman-container-Konsole.

```
mysql -u root -p
# Login with the root password here
create database blog_backend;
GRANT ALL PRIVILEGES ON blog_backend.* To backend;
```

*In einem produktiven system darf der backend user nicht vollprivilegien haben*

# Stand

- [x] Readme erstellen
- [x] Quarkus projekt aufgebaut mit /blog endpoint
- [ ] MariaDB container
- [ ] Persistieren von Blogdateien