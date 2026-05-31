package com.sentrix.core.metric.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class CurrentMetricsResponse {

    private LocalDateTime timestamp;

    private Map<String, Double> features;
}