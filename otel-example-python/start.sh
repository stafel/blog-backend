# from https://intellitect.com/blog/opentelemetry-metrics-python/
# added envs from https://opentelemetry.io/docs/languages/python/automatic/configuration/
# to target the open telemetry endpoint set exporter_otlp_endpoint to port 4318
# to target the tempo otlp endpoint set exporter_otlp_endpoint to port 4320

export OTEL_PYTHON_LOG_CORRELATION=true
export OTEL_PYTHON_LOG_FORMAT="%(msg)s [span_id=%(span_id)s]"
export OTEL_PYTHON_LOG_LEVEL=debug
export OTEL_PYTHON_LOGGING_AUTO_INSTRUMENTATION_ENABLED=true
opentelemetry-instrument \
  --traces_exporter otlp \
  --metrics_exporter otlp \
  --logs_exporter otlp \
  --metric_export_interval 5000 \
  --exporter_otlp_protocol 'http/protobuf' \
  --exporter_otlp_endpoint 'http://localhost:4318' \
  --service_name uvicorn-test \
  uvicorn main:app