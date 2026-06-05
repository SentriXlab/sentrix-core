package com.sentrix.core.metric.controller;

import com.sentrix.core.metric.dto.CurrentMetricsResponse;
import com.sentrix.core.metric.dto.MetricsBufferStatusResponse;
import com.sentrix.core.metric.service.MetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "metric-controller", description = "Metric 수집 및 Buffer 상태 API")
@RestController
public class MetricController {

    private final MetricService metricService;

    public MetricController(MetricService metricService) {
        this.metricService = metricService;
    }

    @Operation(summary = "demo-server의 현재 Prometheus metric을 수집해 SentriX feature로 반환한다.")
    @GetMapping("/api/metrics/current")
    public CurrentMetricsResponse getCurrentMetrics() {
        return metricService.getCurrentMetrics();
    }

    @Operation(summary = "현재 metric을 수동으로 1회 수집하고 Sliding Window Buffer에 저장한다.")
    @PostMapping("/api/metrics/collect")
    public CurrentMetricsResponse collectMetrics() {
        return metricService.collectMetrics();
    }

    @Operation(summary = "Sliding Window Buffer의 현재 상태를 조회한다.")
    @GetMapping("/api/metrics/buffer/status")
    public MetricsBufferStatusResponse getBufferStatus() {
        return metricService.getBufferStatus();
    }
}