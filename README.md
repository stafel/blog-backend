# blog-backend

Blog backend in Java Quarkus für das HFTM Modul IN306 Verteilte Systeme.
In einer nachfolgenden Refaktorierung wurde das Projekt mit open telemetry ergänzt.

Siehe file [telemetry.md](./telemetry.md) für den Aufbau des generell nutzbaren Telemetrie-Umsystems.

Siehe file [quarkus-otel.md](./quarkus-otel.md) für die Dokumentation zur Nachrüstung eines Quarkusprojektes mit open telemetry.

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

*Wichtig: um mit einem MariaDB container betrieben zu werden muss die environment variable QUARKUS_DATASOURCE_PASSWORD gesetzt werden. Am einfachsten geht dies mit einem .env file im blog-backend directory. Testweise kann dies auch per export gesetzt werden*

```
export QUARKUS_DATASOURCE_PASSWORD=YourPassHere
```


## Bauen des Containerimages

```
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## Einrichten der Hilfs-Cotainer

Erstellen des Netzwerks

```
podman network create blog-nw
```

### Erstellen eines MariaDB containers auf Podman

Erstellen einen mariadb containers.

```
podman run -d --name=backend-mariadb -p 9001:3306 --network blog-nw -e MARIADB_USER=backend -e MARIADB_PASSWORD=YourPassHere -e MARIADB_ROOT_PASSWORD=YourPassHere docker.io/mariadb:10.11
```

Erstellen der Datenbank und vergeben der Rechte auf der podman-container-Konsole.

Sie können dabei auch per podman exec befehl auf das container terminal zugreifen. 
```
podman exec -t -i backend-mariadb /bin/sh
```

```
mariadb -u root -p
# Login with the root password here
create database blog_backend;
create user backend@blog_backend identified by 'YourPassHere';
GRANT ALL PRIVILEGES ON blog_backend.* To backend;
exit
exit
```

*In einem produktiven system darf der backend user nicht vollprivilegien haben*

### Erstellen eines MySQL containers auf Podman

TODO 
https://moodle.hftm.ch/mod/page/view.php?id=206192
https://quarkus.io/guides/datasource

### Erstellen eines Keycloak containers auf Podman

Starten des Keycloak containers

```
podman run -d --name blog-keycloak -p 8080:8080 --network blog-nw -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=super$ecret1! quay.io/keycloak/keycloak:latest start-dev
```

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
  - [X] API get, post
  - [ ] Persistenz
  - [ ] Dokumentiert
- [ ] Containerisierung
  - [X] Containerisieren
  - [X] Publish
  - [ ] Dokumentation Keycloak
- [ ] MySQL db
  - [X] Dependencies
  - [X] Migrationsfile
  - [ ] MySQL container
  - [ ] Publish
  
# Berechtigungskonzept

## Rollen

- Admin: Kann alle Blogposts und Kommentare löschen. Kann die Blog-Metadaten editieren. Kann User erstellen und löschen.
- Author: Kann Blogposts verfassen. Kann eigene Blogposts editieren und löschen.
- User: Kann Kommentare zu einem Blogpost erfassen. Kann eigene Kommentare editieren und löschen. Kann eigene Userdaten editieren und sich selbst löschen.

## Endpoints

- /blog
  - GET: Offen
  - POST: Admin-Rolle
- /blog/posts
  - GET: Offen
  - POST: Author-Rolle
- /blog/posts/{id}
  - GET: Offen
  - PUT: Author-Rolle und Author stimmen überrein
  - DELETE: Author-Rolle und Author stimmen überrein oder Admin-Rolle
- /blog/posts/{id}/comments
  - GET: Offen
  - POST: User-Rolle
- /blog/posts/{id}/comments/{id}
  - GET: Offen
  - PUT: User-Rolle und User stimmen überrein
  - DELETE: User-Rolle und User stimmen überrein oder Admin-Rolle
- /blog/users
  - GET: Offen
  - POST: Admin-Rolle
- /blog/users/{id}
  - GET: Offen
  - PUT: User-Rolle und User stimmt überein
  - DELETE: User-Rolle und User stimmt überein oder Admin-Rolle

# Container-Image Build, Package and Publish

Alle Befehle im Verzeichnis *blog-Backend* ausführen.

Erstellen der Artefakte in src/target mit Befehl

```
./mvnw package
```

Erstellen des container images via podman mit

```
podman build -f src/main/docker/Dockerfile.jvm -t ghcr.io/stafel/blog-backend:latest .
```

Optional falls nicht bereits getan: Login auf ghcr.io per

```
podman login ghcr.io -u stafel
```


Pushen nach ghcr.io mit

```
podman push ghcr.io/stafel/blog-backend:latest
```

Package sollte nun unter dem blog-backend repository in github verfügbar sein.