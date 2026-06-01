package com.sentrix.core.diagnosis.controller;

import com.sentrix.core.diagnosis.dto.DiagnosisRunResponse;
import com.sentrix.core.diagnosis.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "diagnosis-controller", description = "진단 실행 API")
@RestController
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @Operation(summary = "현재 window 데이터를 기반으로 이상탐지 및 fault type 진단을 실행한다.")
    @PostMapping("/api/diagnosis/run")
    public DiagnosisRunResponse runDiagnosis() {
        return diagnosisService.runDiagnosis();
    }
}