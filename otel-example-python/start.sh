# from https://intellitect.com/blog/opentelemetry-metrics-python/

export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument \
  --traces_exporter otlp \
  --metrics_exporter otlp \
  --logs_exporter otlp \
  --metric_export_interval 5000 \
  --exporter_otlp_protocol 'http/protobuf' \
  --exporter_otlp_endpoint 'http://10.10.1.10:4318' \
  --service_name uvicorn-test \
  uvicorn main:app