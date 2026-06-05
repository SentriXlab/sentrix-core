package com.sentrix.core.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardFaultDistributionResponse {

    private List<FaultCount> items;

    @Getter
    @AllArgsConstructor
    public static class FaultCount {
        private String faultType;
        private int count;
    }
}