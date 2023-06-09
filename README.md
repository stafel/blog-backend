# blog-backend

Blog backend in Java Quarkus für das HFTM Modul IN306 Verteilte Systeme.

# Starten und Testen

Projekt kann im Verzeichnis blog-backend  gestartet werden mit dem Befehl:

```
./mvnw quarkus:dev
```

Auf der Quarkus-Konsole kann mit *r* das Testing gestartet werden.

*Wichtig: um mit podman betrieben zu werden muss die DOCKER_HOST env gesetzt werden*

Siehe [guide quarkus with podman](https://quarkus.io/guides/podman).

```
export DOCKER_HOST=unix:///run/user/1000/podman/podman.sock
```

*Wichtig: um mit einem MariaDB container betrieben zu werden muss die environment variable QUARKUS_DATASOURCE_PASSWORD gesetzt werden. Am einfachsten geht dies mit einem .env file im blog-backend directory*

## Erstellen eines MariaDB containers auf Podman

Erstellen einen mariadb containers.

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
- [X] MariaDB container erstellt
- [X] MariaDB container angebunden
- [X] Persistieren von Blogdateien
- [X] Blogposts abfragen, erstellen, löschen per REST
- [ ] Bug DB wird neu erstellt bei jedem Start und löscht bestehende Daten
- [X] [OpenAPI spec](/docs/openapi.json) view [in the swagger editor](https://editor.swagger.io/)
  - [X] PUT Request zum updaten von blog, post, user
  - [X] Query-Parameter zur filterung von posts
  - [X] Fehler-Responses beim Versuch eine nicht vorhandene Resource zu löschen
  - [X] Linking nach [RFC5988](https://datatracker.ietf.org/doc/html/rfc5988) for [HATEOAS](https://restfulapi.net/hateoas/)
- [ ] OpenAPI spec umgesetzt
  -[X] PUT request
  -[X] Query params
  -[X] Fehler response
  -[ ] Links
- [ ] Blog kommentar als DTO
  - [ ] API get, post
  - [ ] Persistenz
  - [ ] Dokumentiert