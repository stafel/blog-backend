# blog-backend

Blog backend in Java Quarkus für das HFTM Modul IN306 Verteilte Systeme.
In einer nachfolgenden Refaktorierung wurde das Projekt mit open telemetry ergänzt.

Siehe Kapitel [Quickstart](#Quickstart) für einen einfachen Aufbau eines Testfalles mit Open Telemetry Tracing.

Siehe file [telemetry.md](./telemetry.md) für den Aufbau des generell nutzbaren Telemetrie-Umsystems.

Siehe file [quarkus-otel.md](./quarkus-otel.md) für die Dokumentation zur Nachrüstung eines bestehenden Quarkusprojektes mit open telemetry.

Siehe file [dev.md](./dev.md) für Hinweise zur Entwicklung an diesem Projekt.

# Quickstart

Erstellen sie das Netzwerk und starten sie die benötigten Container der Applikation (MariaDB, Keycloak, Quarkus Backend) und der OpenTelemetry All-in-one-Container mit folgenden Befehlen.

Docker-Befehle:

```
docker network create blog-nw

docker run -d --name=backend-mariadb -p 9001:3306 --network blog-nw -v ./mariadb/init.sql:/data/application/init.sql -e MARIADB_USER=backend -e MARIADB_PASSWORD=YourPassHere -e MARIADB_ROOT_PASSWORD=YourPassHere docker.io/mariadb:10.11 --init-file /data/application/init.sql

docker run -d --name blog-keycloak -p 8080:8080 --network blog-nw -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=super$ecret1! quay.io/keycloak/keycloak:latest start-dev

docker run -d --name otel-aio -p 3000:3000 -p 4317:4317 -p 4318:4318  -v ./otel/d_aio_config.yaml:/otel-lgtm/otelcol-config.yaml --network blog-nw docker.io/grafana/otel-lgtm

docker run -d --name blog-backend -p 8085:8085 -e quarkus.datasource.jdbc.url=jdbc:mariadb://host.docker.internal:9001/blog_backend -e quarkus.otel.exporter.otlp.traces.endpoint=http://host.docker.internal:4317  --network blog-nw ghcr.io/stafel/blog-backend:latest
```

Podman-Befehle:

```
podman network create blog-nw

podman run -d --name=backend-mariadb -p 9001:3306 --network blog-nw -v ./mariadb/init.sql:/data/application/init.sql -e MARIADB_USER=backend -e MARIADB_PASSWORD=YourPassHere -e MARIADB_ROOT_PASSWORD=YourPassHere docker.io/mariadb:10.11 --init-file /data/application/init.sql

podman run -d --name blog-keycloak -p 8080:8080 --network blog-nw -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=super$ecret1! quay.io/keycloak/keycloak:latest start-dev

podman run -d --name otel-aio -p 3000:3000 -p 4317:4317 -p 4318:4318 -v ./otel/p_aio_config.yaml:/otel-lgtm/otelcol-config.yaml --network blog-nw docker.io/grafana/otel-lgtm

podman run -d --name blog-backend -p 8085:8085 -e quarkus.datasource.jdbc.url=jdbc:mariadb://host.containers.internal:9001/blog_backend -e quarkus.otel.exporter.otlp.traces.endpoint=http://host.containers.internal:4317  --network blog-nw ghcr.io/stafel/blog-backend:latest
```

Warten sie eine Minute bis der OpenTelemetry stack aufgesetzt ist im Container "otel-aio" und die Logmessage "The OpenTelemetry collector and the Grafana LGTM stack are up and running." erscheint.

Es ist nun die Quarkus-Konsole auf [http://localhost:8085/](http://localhost:8085/) und das Grafana-Dashboard unter [http://localhost:3000/login](http://localhost:3000/login) sichtbar.

Loggen sie sich in Grafana mit Username "admin" und Passwort "admin" ein.

Wenn sie nun einen blog-endpoint ansprechen (z.B. [http://localhost:8085/blog](http://localhost:8085/blog)) erhalten sie einen Trace in der [Tempo-Datasource in Grafana](http://localhost:3000/explore?schemaVersion=1&panes=%7B%22TCD%22:%7B%22datasource%22:%22tempo%22,%22queries%22:%5B%7B%22refId%22:%22A%22,%22datasource%22:%7B%22type%22:%22tempo%22,%22uid%22:%22tempo%22%7D,%22queryType%22:%22traceqlSearch%22,%22limit%22:20,%22tableType%22:%22traces%22,%22filters%22:%5B%7B%22id%22:%226235affa%22,%22operator%22:%22%3D%22,%22scope%22:%22span%22%7D%5D%7D%5D,%22range%22:%7B%22from%22:%22now-1h%22,%22to%22:%22now%22%7D%7D%7D&orgId=1), ebenfalls werden die Metriken in der [Prometheus-Datasource in Grafana](http://localhost:3000/explore?schemaVersion=1&panes=%7B%229vQ%22%3A%7B%22datasource%22%3A%22prometheus%22%2C%22queries%22%3A%5B%7B%22refId%22%3A%22A%22%2C%22expr%22%3A%22target_info%22%2C%22range%22%3Atrue%2C%22instant%22%3Atrue%2C%22datasource%22%3A%7B%22type%22%3A%22prometheus%22%2C%22uid%22%3A%22prometheus%22%7D%2C%22editorMode%22%3A%22builder%22%2C%22legendFormat%22%3A%22__auto%22%2C%22useBackend%22%3Afalse%2C%22disableTextWrap%22%3Afalse%2C%22fullMetaSearch%22%3Afalse%2C%22includeNullMetadata%22%3Atrue%7D%5D%2C%22range%22%3A%7B%22from%22%3A%22now-1h%22%2C%22to%22%3A%22now%22%7D%7D%7D&orgId=1) dargestellt.

Unsere Custom-Metrik ["blog_requests_connections_total" ist ebenfalls in Prometheus](http://localhost:3000/explore?schemaVersion=1&panes=%7B%229vQ%22%3A%7B%22datasource%22%3A%22prometheus%22%2C%22queries%22%3A%5B%7B%22refId%22%3A%22A%22%2C%22expr%22%3A%22blog_requests_connections_total%22%2C%22range%22%3Atrue%2C%22instant%22%3Atrue%2C%22datasource%22%3A%7B%22type%22%3A%22prometheus%22%2C%22uid%22%3A%22prometheus%22%7D%2C%22editorMode%22%3A%22builder%22%2C%22legendFormat%22%3A%22__auto%22%2C%22useBackend%22%3Afalse%2C%22disableTextWrap%22%3Afalse%2C%22fullMetaSearch%22%3Afalse%2C%22includeNullMetadata%22%3Atrue%7D%5D%2C%22range%22%3A%7B%22from%22%3A%22now-1h%22%2C%22to%22%3A%22now%22%7D%7D%7D&orgId=1)


  
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