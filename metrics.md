# Prometheus

Als Startpunkt gilt das image mit Anleitung auf [dockerhub](https://hub.docker.com/r/prom/prometheus/#!), dabei müssen aber noch die Volumes für die Datenpersistierung beachtet werden wie in dieser [Frage von Matt und Antwort von Robert auf stackexchange](https://stackoverflow.com/questions/50009065/how-to-persist-data-in-prometheus-running-in-a-docker-container) detailiert. Siehe auch das [Dockerfile für Prometheus](https://github.com/prometheus/prometheus/blob/main/Dockerfile) für das Volume.

Das angegebene config file prometheus.yml entstammt dem [getting started tutorial von prometheus](https://prometheus.io/docs/prometheus/latest/getting_started/)

Vollständige Befehle zum erstellen eines Volumens und starten eines persistierten Prometheus containers sieht somit folgendermassen aus:

```
podman volume create prometheus-data
podman run -d --name prometheus \
    -p 9111:9090 \
    -v prometheus-data:/prometheus \
    -v "$(pwd)"/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
    docker.io/prom/prometheus
```

Es sollte nun die webkonsole unter der addresse [localhost:9111](http://localhost:9111) aufrufbar sein.