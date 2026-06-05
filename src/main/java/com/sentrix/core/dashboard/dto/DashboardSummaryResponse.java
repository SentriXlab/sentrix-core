package com.sentrix.core.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DashboardSummaryResponse {

    private String systemStatus;
    private double latestAnomalyScore;
    private double threshold;
    private String latestFaultType;
    private double latestConfidence;
    private String modelServerStatus;
    private LocalDateTime lastDiagnosisAt;
}