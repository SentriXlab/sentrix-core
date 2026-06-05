package com.sentrix.core.dashboard.controller;

import com.sentrix.core.dashboard.dto.DashboardEvidenceResponse;
import com.sentrix.core.dashboard.dto.DashboardFaultDistributionResponse;
import com.sentrix.core.dashboard.dto.DashboardSummaryResponse;
import com.sentrix.core.dashboard.dto.DashboardTimelineResponse;
import com.sentrix.core.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "dashboard-controller", description = "대시보드 API")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "대시보드 요약 정보 조회")
    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardService.getSummary();
    }

    @Operation(summary = "anomaly score 타임라인 조회")
    @GetMapping("/timeline")
    public DashboardTimelineResponse getTimeline() {
        return dashboardService.getTimeline();
    }

    @Operation(summary = "fault type 분포 조회")
    @GetMapping("/faults/distribution")
    public DashboardFaultDistributionResponse getFaultDistribution() {
        return dashboardService.getFaultDistribution();
    }

    @Operation(summary = "최신 판단 근거 feature 조회")
    @GetMapping("/evidence/latest")
    public DashboardEvidenceResponse getLatestEvidence() {
        return dashboardService.getLatestEvidence();
    }
}