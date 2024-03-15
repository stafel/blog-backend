# Setup

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
    docker.io/prom/prometheus
```

Es sollte nun die webkonsole unter der addresse [http://localhost:9111](http://localhost:9111) aufrufbar sein.

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

### Setup Data source

Mit einer aktiven Prometheus instanz kann nun unter dem Menüpunkt "Connections/Data sources" eine neue Prometheus datasource ausgewählt werden, [direktlink http://localhost:3500/connections/datasources/prometheus](http://localhost:3500/connections/datasources/prometheus).

Dabei muss lediglich die Connection/Prometheus server URL mit "http://host.containers.internal:9111" angegeben werden. Für mehr info über die Prometheus connection siehe die [grafana dokumentation](https://grafana.com/docs/grafana/latest/datasources/prometheus/configure-prometheus-data-source/)

### Setup service account für Prometheus remote write

**Alternative Verbindungsmöglichkeit zur Data source wird in diesem Tutorial nicht aktiv verwendet** 

Nach dem Passwortwechsel sind sie im Dashboard. Navigieren sie zu den Serviceaccounts unter Administration/Users and access/Service accounts oder mit dem [direktlink http://localhost:3500/org/serviceaccounts](http://localhost:3500/org/serviceaccounts) und erstellen sie den von Prometheus genutzen user.

Display name: prometheus

Role: Editor

In der neuen Service account übersicht erstellen sie nun ein Service Token mit dem Button "Add service account token" und im popup "Generate token". Das angezeigte Token muss nun in das file prometheus/prometheus.yml hier im repository in das feld remote_write/basic_auth/password (vorgefüllt mit "\<Your Grafana.com API Key\>") kopiert werden.