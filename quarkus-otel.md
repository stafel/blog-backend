# Big Picture

Da die momentane implementation von opentelemetry in quarkus noch kein tracing und metrics unterstützt ([siehe infopanel des offiziellen guides](https://quarkus.io/guides/opentelemetry)) wird für die vollständige Telemetrieübertragung nebst opentelemetry auch auf micrometer für die metriken eingesetzt. Logs werden per Loki Docker Logging Driver verarbeitet. 

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

```
import org.jboss.logging.Logger;

[...]

@Path("blog")
public class BlogResource {
    @Inject
    Logger log;

    [...]

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Blog getBlog() {
        log.info("Getting blog");
        Blog blog = blogRepository.getBlog();
        [...]
    }
    [...]
}
```