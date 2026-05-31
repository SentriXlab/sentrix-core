package com.sentrix.core.metric.service;

import com.sentrix.core.metric.collector.PrometheusMetricCollector;
import com.sentrix.core.metric.dto.CurrentMetricsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricService {

    private final PrometheusMetricCollector prometheusMetricCollector;

    public CurrentMetricsResponse getCurrentMetrics() {
        Map<String, Double> features = prometheusMetricCollector.collectCurrentMetrics();

        return CurrentMetricsResponse.builder()
                .timestamp(LocalDateTime.now())
                .features(features)
                .build();
    }
}