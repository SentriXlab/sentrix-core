package com.sentrix.core.metric.controller;

import com.sentrix.core.metric.dto.CurrentMetricsResponse;
import com.sentrix.core.metric.service.MetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricController {

    private final MetricService metricService;

    @GetMapping("/current")
    public CurrentMetricsResponse getCurrentMetrics() {
        return metricService.getCurrentMetrics();
    }
}