package com.sentrix.core.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardEvidenceResponse {

    private String faultType;
    private List<EvidenceFeature> evidenceFeatures;

    @Getter
    @AllArgsConstructor
    public static class EvidenceFeature {
        private String featureName;
        private double score;
    }
}