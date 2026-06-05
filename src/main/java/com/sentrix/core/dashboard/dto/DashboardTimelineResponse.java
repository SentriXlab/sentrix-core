package com.sentrix.core.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardTimelineResponse {

    private List<TimelinePoint> points;

    @Getter
    @AllArgsConstructor
    public static class TimelinePoint {
        private LocalDateTime timestamp;
        private double anomalyScore;
        private double threshold;
        private String status;
    }
}