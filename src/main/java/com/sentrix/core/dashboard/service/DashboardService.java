package com.sentrix.core.dashboard.service;

import com.sentrix.core.dashboard.dto.DashboardEvidenceResponse;
import com.sentrix.core.dashboard.dto.DashboardFaultDistributionResponse;
import com.sentrix.core.dashboard.dto.DashboardSummaryResponse;
import com.sentrix.core.dashboard.dto.DashboardTimelineResponse;
import com.sentrix.core.diagnosis.dto.DiagnosisRunResponse;
import com.sentrix.core.diagnosis.service.DiagnosisService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DashboardService {

    private final DiagnosisService diagnosisService;
    private final List<DiagnosisRunResponse> diagnosisHistory = new CopyOnWriteArrayList<>();

    public DashboardService(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    public void saveDiagnosisResult(DiagnosisRunResponse response) {
        diagnosisHistory.add(response);
        if (diagnosisHistory.size() > 100) {
            diagnosisHistory.remove(0);
        }
    }

    public DashboardSummaryResponse getSummary() {
        if (diagnosisHistory.isEmpty()) {
            return new DashboardSummaryResponse("UNKNOWN", 0.0, 0.0, "UNKNOWN", 0.0, "UP", null);
        }
        DiagnosisRunResponse latest = diagnosisHistory.get(diagnosisHistory.size() - 1);
        return new DashboardSummaryResponse(
                latest.getDetectionStatus(), latest.getAnomalyScore(), latest.getThreshold(),
                latest.getFaultType(), latest.getConfidence(), "UP", latest.getTimestamp()
        );
    }

    public DashboardTimelineResponse getTimeline() {
        List<DashboardTimelineResponse.TimelinePoint> points = new ArrayList<>();
        for (DiagnosisRunResponse r : diagnosisHistory) {
            points.add(new DashboardTimelineResponse.TimelinePoint(
                    r.getTimestamp(), r.getAnomalyScore(), r.getThreshold(), r.getDetectionStatus()
            ));
        }
        return new DashboardTimelineResponse(points);
    }

    public DashboardFaultDistributionResponse getFaultDistribution() {
        java.util.Map<String, Integer> countMap = new java.util.HashMap<>();
        for (DiagnosisRunResponse r : diagnosisHistory) {
            countMap.merge(r.getFaultType(), 1, Integer::sum);
        }
        List<DashboardFaultDistributionResponse.FaultCount> items = new ArrayList<>();
        countMap.forEach((k, v) -> items.add(new DashboardFaultDistributionResponse.FaultCount(k, v)));
        return new DashboardFaultDistributionResponse(items);
    }

    public DashboardEvidenceResponse getLatestEvidence() {
        if (diagnosisHistory.isEmpty()) {
            return new DashboardEvidenceResponse("UNKNOWN", new ArrayList<>());
        }
        DiagnosisRunResponse latest = diagnosisHistory.get(diagnosisHistory.size() - 1);
        String faultType = latest.getFaultType();

        java.util.Map<String, List<String>> evidenceMap = new java.util.HashMap<>();
        evidenceMap.put("HIGH_CPU", List.of("process_cpu_usage_mean", "process_cpu_usage_max"));
        evidenceMap.put("MEMORY_PRESSURE", List.of("jvm_memory_max_ratio_max", "jvm_memory_used_slope"));
        evidenceMap.put("LATENCY_SPIKE", List.of("latency_p99_max", "latency_p95_mean"));
        evidenceMap.put("ERROR_SPIKE", List.of("error_rate_max", "latency_p95_max"));

        List<String> featureNames = evidenceMap.getOrDefault(faultType, new ArrayList<>());
        List<DashboardEvidenceResponse.EvidenceFeature> features = new ArrayList<>();
        for (String name : featureNames) {
            features.add(new DashboardEvidenceResponse.EvidenceFeature(name, 0.0));
        }
        return new DashboardEvidenceResponse(faultType, features);
    }
}