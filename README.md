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

# Stand

- [x] Readme erstellen
- [x] Quarkus projekt aufgebaut mit /blog endpoint