# Big Picture

Da die momentane implementation von opentelemetry in quarkus noch kein tracing und metrics unterstützt ([siehe infopanel des offiziellen guides](https://quarkus.io/guides/opentelemetry)) wird für die vollständige Telemetrieübertragung neben opentelemetry auch auf micrometer für die metriken eingesetzt. 

Logs werden per Loki Docker Logging Driver verarbeitet siehe [telemetry.md kapitel "Loki als Docker logging driver einsetzen"](./telemetry.md#loki-als-docker-logging-driver-einsetzen). Falls das nicht möglich ist besteht die Alternative per file log handler die daten in ein file zu schreiben (Anleitung im [quarkus logging quide](https://quarkus.io/guides/logging)) und diese per [promtail](https://grafana.com/docs/loki/latest/send-data/promtail/) zu extrahieren und auf loki zu pushen.

Nachrüstung basiert auf dem [offiziellen Quarkus-OpenTelemetry-Guide](https://quarkus.io/guides/opentelemetry) und dem [offiziellen Quarkus-Micrometer-Guide](https://quarkus.io/guides/telemetry-micrometer-tutorial)

## Dependency

Damit mit opentelemetry tracing gearbetet werden kann muss die dependency ```quarkus-opentelemetry``` im pom.xml aufgenommen werden.

Damit ein prometheus endpoint erstellt werden kann wir die dependency ```quarkus-micrometer-registry-prometheus``` aufgenommen. Es sorgt dafür, dass unter dem endpoint [/q/metrics](http://localhost:8085/q/metrics) die prometheus metriken exportiert werden.

Ebenfalls wird, falls noch nicht vorhanden, ein logging framework benötigt, in diesem Fall jBoss wie in der [offiziellen Quarkus-Logging-Guide](https://quarkus.io/guides/logging) dokumentiert.

```
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-opentelemetry</artifactId>
</dependency>
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>
<dependency>
    <groupId>org.jboss.logging</groupId>
    <artifactId>jboss-logging</artifactId>
</dependency>
```

## Application Properties

Damit nun die open telemetry daten an ein otlp endpoint gesendet werden müssen dafür die entsprechenden Variablen in resources/application.properties gesetzt werden.

```
# Tracing settings
quarkus.application.name=blog-backend 
quarkus.otel.exporter.otlp.traces.endpoint=http://host.containers.internal:4317 
quarkus.log.console.format=%d{HH:mm:ss} %-5p traceId=%X{traceId}, parentId=%X{parentId}, spanId=%X{spanId}, sampled=%X{sampled} [%c{2.}] (%t) %s%e%n  
```

Ebenfalls kann hier das Loglevel angegeben werden.

```
quarkus.log.level=INFO
```

## Nachrüsten eines Loggers

Wir können nun den jBoss logger importieren und in den API-Klassen injecten lassen.

Siehe Beispiel in [BlogResource](./blog-backend/src/main/java/ch/hftm/API/BlogResource.java) in welcher wir Begin, Wechsel auf Sekundäre Daten und Ende der Methode ins Log kommentieren.

```java
import org.jboss.logging.Logger;

@Path("blog")
public class BlogResource {
    @Inject
    Logger log;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Blog getBlog() {
        log.info("Getting blog");
        Blog blog = blogRepository.getBlog();
    }
}
```

## Einfügen von eigenen Metriken

Entnommen aus der [offiziellen Quarkus Micrometer guide](https://quarkus.io/guides/telemetry-micrometer) und [Micrometer dokumentation](https://docs.micrometer.io/micrometer/reference/concepts/counters.html).

Zum definieren der eigenen Metriken muss zuerst eine Referenz zum MeterRegistry erlangt werden, falls kein eigenes Registry definiert wurde wird dies am einfachsten über die "Metrics.globalRegistry"-Referenz erreicht.

Danach können die Metrik-Objekte registriert werden.

 - Gauges: steigend oder sinkende metriken wie z.B. Auslastung
 - Counters: nur steigende metriken wie z.B. Summe aller Requests bisher
 - Timers: Zeitmessung mit Total-Summe, Anzahl an Messungen und Höchstwert
 - Summary: Wertmessung mit Total-Summe, Anzahl an Messung und Höchstwert

Mit einer Referenz auf ein Metrik objekt können danach die Werte verändert werden.

Siehe Beispiel in [BlogRepository](./blog-backend/src/main/java/ch/hftm/Repositories/BlogRepository.java) in welcher wir einen Counter zur Zählung der Abfragen des Blog erstellen.

```java
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {

    MeterRegistry registry = Metrics.globalRegistry;

    public BlogRepository() {
        // a counter for the number of returns of blog
        blogRequests = Counter.builder("blog.requests")
            .baseUnit("connections")
            .description("number of requests to the blog endpoint since startup")
            .tags("blog", "test")
            .register(registry);
    }


    public Blog getBlog() {
        blogRequests.increment(); // here we increment our custom counter
        return this.findById(1L); // lazy method just return the first ever created blog
    }
}
```

Wenn sie nun den Endpoint [http://localhost:8085/q/metrics](http://localhost:8085/q/metrics) prüfen werden sie dort die neue Metrik finden.

Der Name "blog_requests_connections_total" ergibt sich aus dem Grundnamen "blog.requests" (Sonderzeichen werden zu Underscores), der Einheit "connections" und dem typ Metrik "total"

```
...
# HELP blog_requests_connections_total number of requests to the blog endpoint since startup
# TYPE blog_requests_connections_total counter
blog_requests_connections_total{blog="test",} 5.0
...
```