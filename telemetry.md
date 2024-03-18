# Big Picture

Ziel ist es verschiedene Telemetriedaten (Metriken, Logs, Traces) in Grafana zur Verfügung zu stellen um dort diese zu aggregieren und daraus in Dashboards Übersicht zu schaffen.

Da die verschiedenen Telemetriedaten in unterschiedlichen Arten produziert, dargestellt und verarbeitet werden sind mehrere spezialisierte Subservices an Grafana angebunden.

- Prometheus für Metriken
- Tempo für Traces
- Loki für Logs

Davor vorgeschalten ist der OpenTelemetry Collector welcher wiederum ein einheitliches Interface darstellt welches per API verwendet werden kann.

# Containerisierung

Die Anleitung bezieht sich auf den Betrieb der Container auf [Podman](https://podman.io/).

Für Docker können die Befehle equivalent ausgeführt werden, dabei muss nur "podman" durch "docker" ersetzt werden.

Aus ```podman volume create test``` wird somit ```docker volume create test```.

Wird das vollständige Setup durchgespielt anstelle des All-in-One-Containers so müssen in der Anleitung und in den yaml-Files die Verweise auf die Hostmaschine "host.containers.internal" durch "host.docker.internal" ersetzt werden.

# Setup

Eine All-In-One-Implementation für einen schnellen Setup für lokale Tests wird mit dem [grafana docker otel-lgtm](https://github.com/grafana/docker-otel-lgtm) zur Verfügung gestellt. 

Mit folgendem Befehl kann ein open telemetry container erstellt werden.

```
podman run -d --name otel-aio -p 3000:3000 -p 4317:4317 -p 4318:4318 docker.io/grafana/otel-lgtm
```

Nachfolgend finden Sie eine Schritt für Schritt Setup welches die einzelnen Services in separaten Containern betreibt um das ganze in einer bestehenden Umgebung verteilt zu betreiben und mit verschiedenen Interfaces zu befüllen (open telemetry, prometheus metrics websites, promtail log exporter, usw...).

## Prometheus

Als Startpunkt gilt das image mit Anleitung auf [dockerhub](https://hub.docker.com/r/prom/prometheus/#!), dabei müssen aber noch die Volumes für die Datenpersistierung beachtet werden wie in dieser [Frage von Matt und Antwort von Robert auf stackexchange](https://stackoverflow.com/questions/50009065/how-to-persist-data-in-prometheus-running-in-a-docker-container) detailiert. Siehe auch das [Dockerfile für Prometheus](https://github.com/prometheus/prometheus/blob/main/Dockerfile) für das Volume.

Das angegebene config file prometheus.yml entstammt dem [getting started tutorial von prometheus](https://prometheus.io/docs/prometheus/latest/getting_started/) und wurde mit den remote write befehlen für die übertragung nach grafana ergänzt aus dem [getting started with grafana and prometheus tutorial](https://grafana.com/docs/grafana/latest/getting-started/get-started-grafana-prometheus/)

Vollständige Befehle zum erstellen eines Volumens und starten eines persistierten Prometheus containers sieht somit folgendermassen aus:

```
podman volume create prometheus-data
podman run -d --name prometheus \
    -p 9111:9090 \
    -v prometheus-data:/prometheus \
    -v "$(pwd)"/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
    docker.io/prom/prometheus \
    --config.file=/etc/prometheus/prometheus.yml --web.enable-remote-write-receiver 
```

Es sollte nun die webkonsole unter der addresse [http://localhost:9111](http://localhost:9111) aufrufbar sein.

## Loki

Abgewandelte persistenter container nach Vorlage des [tutorials von grafanalabs](https://grafana.com/docs/loki/latest/setup/install/docker/).

```
podman run -d --name loki \
    -p 3100:3100 \
    -v $(pwd)/loki:/mnt/config \
    docker.io/grafana/loki \
    -config.file=/mnt/config/loki-config.yaml
```

Es sollte nun eine readiness-meldung unter der addresse [http://localhost:3100/ready](http://localhost:3100/ready) aufrufbar sein.

Falls auf einem Linux-System gearbeitet wird kann nun auch testweise promtail gestartet werden um die lokale Maschine zu überwachen.

```
podman run -d --name promtail \
    -v $(pwd)/promtail:/mnt/config \
    -v /var/log:/var/log \
    docker.io/grafana/promtail \
    -config.file=/mnt/config/promtail-config.yaml
```

## Tempo

Entnommen aus dem [code beispiel des git repositories](https://github.com/grafana/tempo/blob/main/example/docker-compose/local/docker-compose.yaml) wurden alle ports und konfiguration entfernt welche nicht direkt Tempo und OTLP betreffen.

```
podman volume create tempo-data
podman run -d --name tempo \
    -p 3200:3200 \
    -p 9095:9095 \
    -p 4319:4317 \
    -p 4320:4318 \
    -v tempo-data:/tmp/tempo \
    -v $(pwd)/tempo/tempo.yaml:/etc/tempo.yaml \
    docker.io/grafana/tempo \
    -config.file=/etc/tempo.yaml
```

## Open Telemetry Collector

Aus dem [quick-start tutorial von open telemetry](https://opentelemetry.io/docs/collector/quick-start/) und [collector installation dokumentation von open telemetry](https://opentelemetry.io/docs/collector/installation/). [Beispielkonfiguration des All-in-One images](https://github.com/grafana/docker-otel-lgtm/blob/main/docker/otelcol-config.yaml) als Vorlage verwenet.

Wir verwenden das contrib-image da darin auch die inoffiziellen open source Komponenten enthalten sind wie der loki-Exporter.

Port 55679 wird für die debug webpages von zPages verwendet [http://localhost:55679/debug/servicez](http://localhost:55679/debug/servicez) und [http://localhost:55679/debug/tracez](http://localhost:55679/debug/tracez)

```
podman run -d --name otel-collector \
  -p 4317:4317 \
  -p 4318:4318 \
  -p 55679:55679 \
  -v $(pwd)/otel/config.yaml:/etc/otelcol-contrib/config.yaml \
  docker.io/otel/opentelemetry-collector-contrib \
  --config=/etc/otelcol-contrib/config.yaml
```

## Grafana

Wie in der [grafana docker dokumentation](https://grafana.com/docs/grafana/latest/setup-grafana/installation/docker/) beschrieben wird eine persistierte Instanz der OSS-Version erstellt.

```
podman volume create grafana-data
podman run -d --name grafana \
    -p 3500:3000 \
    -v grafana-data:/var/lib/grafana \
    docker.io/grafana/grafana-oss
```

Es sollte nun die webkonsole unter der addresse [http://localhost:3500/login](http://localhost:3500/login) aufrufbar sein.

Mit dem user "admin" und passwort "admin" kann nun eingeloggt und ein neues sicheres passwort gewählt werden, sie [sing in to grafana](https://grafana.com/docs/grafana/latest/setup-grafana/sign-in-to-grafana/).

### Setup Data source Prometheus

Mit einer aktiven Prometheus instanz kann nun unter dem Menüpunkt "Connections/Data sources" eine neue Prometheus data source ausgewählt werden, [direktlink http://localhost:3500/connections/datasources/prometheus](http://localhost:3500/connections/datasources/prometheus).

Dabei muss lediglich die Connection/Prometheus server URL mit "http://host.containers.internal:9111" angegeben werden. Für mehr info über die Prometheus connection siehe die [grafana dokumentation](https://grafana.com/docs/grafana/latest/datasources/prometheus/configure-prometheus-data-source/)

### Setup Data source Loki

Gleiches vorgehen wie bei der Prometheus Data source.
Mit einer aktiven Loki instanz kann nun unter dem Menüpunkt "Connections/Data sources" eine neue Loki data source ausgewählt werden, [direktlink http://localhost:3500/connections/datasources/loki](http://localhost:3500/connections/datasources/loki).

Dabei muss lediglich die Connection/URL mit "http://host.containers.internal:3100" angegeben werden. Für mehr info über die Loki connection siehe die [grafana dokumentation](https://grafana.com/docs/grafana/latest/datasources/loki/configure-loki-data-source/)

### Setup Data source Tempo

Gleiches vorgehen wie bei der Prometheus Data source.
Mit einer aktiven Tempo instanz kann nun unter dem Menüpunkt "Connections/Data sources" eine neue Tempo data source ausgewählt werden, [direktlink http://localhost:3500/connections/datasources/tempo](http://localhost:3500/connections/datasources/tempo).

Dabei muss lediglich die Connection/URL mit "http://host.containers.internal:3200" angegeben werden. Für mehr info über die Tempo connection siehe die [grafana dokumentation](https://grafana.com/docs/grafana/latest/datasources/tempo/configure-tempo-data-source/)

### Setup service account für Prometheus remote write

**Alternative Verbindungsmöglichkeit zur Data source wird in diesem Tutorial nicht aktiv verwendet** 

Nach dem Passwortwechsel sind sie im Dashboard. Navigieren sie zu den Serviceaccounts unter Administration/Users and access/Service accounts oder mit dem [direktlink http://localhost:3500/org/serviceaccounts](http://localhost:3500/org/serviceaccounts) und erstellen sie den von Prometheus genutzen user.

Display name: prometheus

Role: Editor

In der neuen Service account übersicht erstellen sie nun ein Service Token mit dem Button "Add service account token" und im popup "Generate token". Das angezeigte Token muss nun in das file prometheus/prometheus.yml hier im repository in das feld remote_write/basic_auth/password (vorgefüllt mit "\<Your Grafana.com API Key\>") kopiert werden.