package com.sentrix.core.metric.collector;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PrometheusMetricCollector {

    private final WebClient.Builder webClientBuilder;

    @Value("${sentrix.demo-server.actuator-url:http://localhost:8080/actuator/prometheus}")
    private String actuatorUrl;

    public Map<String, Double> collectCurrentMetrics() {
        String prometheusText = webClientBuilder.build()
                .get()
                .uri(actuatorUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<String, Double> features = new HashMap<>();

        features.put("process_cpu_usage", extractSimpleMetric(prometheusText, "process_cpu_usage"));
        features.put("system_cpu_usage", extractSimpleMetric(prometheusText, "system_cpu_usage"));
        features.put("jvm_gc_overhead", extractSimpleMetric(prometheusText, "jvm_gc_overhead_percent"));
        features.put("jvm_threads_live", extractSimpleMetric(prometheusText, "jvm_threads_live_threads"));
        features.put("db_connections_active", extractSimpleMetric(prometheusText, "jdbc_connections_active"));

        double jvmMemoryUsed = sumMetricByPrefix(prometheusText, "jvm_memory_used_bytes");
        double jvmMemoryMax = sumMetricByPrefix(prometheusText, "jvm_memory_max_bytes");

        features.put("jvm_memory_used", jvmMemoryUsed);
        features.put("jvm_memory_max_ratio", jvmMemoryMax > 0 ? jvmMemoryUsed / jvmMemoryMax : 0.0);

        double requestCount = sumMetricByPrefix(prometheusText, "http_server_requests_seconds_count");
        double requestSum = sumMetricByPrefix(prometheusText, "http_server_requests_seconds_sum");

        features.put("request_count_total", requestCount);
        features.put("latency_avg", requestCount > 0 ? requestSum / requestCount : 0.0);

        double errorCount = sumMetricByStatusPattern(prometheusText, "http_server_requests_seconds_count", "5");
        features.put("error_rate", requestCount > 0 ? errorCount / requestCount : 0.0);

        return features;
    }

    private double extractSimpleMetric(String text, String metricName) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }

        String[] lines = text.split("\\R");

        for (String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }

            if (line.startsWith(metricName + " ")) {
                return parseLastNumber(line);
            }

            if (line.startsWith(metricName + "{")) {
                return parseLastNumber(line);
            }
        }

        return 0.0;
    }

    private double sumMetricByPrefix(String text, String metricName) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }

        double sum = 0.0;
        String[] lines = text.split("\\R");

        for (String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }

            if (line.startsWith(metricName + " ") || line.startsWith(metricName + "{")) {
                sum += parseLastNumber(line);
            }
        }

        return sum;
    }

    private double sumMetricByStatusPattern(String text, String metricName, String statusPrefix) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }

        double sum = 0.0;
        String[] lines = text.split("\\R");

        for (String line : lines) {
            if (line.startsWith("#")) {
                continue;
            }

            if (!line.startsWith(metricName + "{")) {
                continue;
            }

            if (line.contains("status=\"" + statusPrefix)) {
                sum += parseLastNumber(line);
            }
        }

        return sum;
    }

    private double parseLastNumber(String line) {
        try {
            String[] tokens = line.trim().split("\\s+");
            return Double.parseDouble(tokens[tokens.length - 1]);
        } catch (Exception e) {
            return 0.0;
        }
    }
}