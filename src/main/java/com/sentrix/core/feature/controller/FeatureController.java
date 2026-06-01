package com.sentrix.core.feature.controller;

import com.sentrix.core.feature.dto.FeatureSchemaResponse;
import com.sentrix.core.feature.dto.ModelInputFeaturesResponse;
import com.sentrix.core.feature.dto.WindowFeaturesResponse;
import com.sentrix.core.feature.service.FeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "feature-controller", description = "Feature 계산 및 변환 API")
@RestController
public class FeatureController {

    private final FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @Operation(summary = "Sliding Window Buffer 기반 window feature를 계산한다.")
    @GetMapping("/api/features/window")
    public WindowFeaturesResponse getWindowFeatures() {
        return featureService.calculateWindowFeatures();
    }

    @Operation(summary = "현재 SentriX Core에서 사용하는 feature schema를 조회한다.")
    @GetMapping("/api/features/schema")
    public FeatureSchemaResponse getFeatureSchema() {
        return featureService.getFeatureSchema();
    }

    @Operation(summary = "model-server에 전달될 55개 입력 feature를 조회한다.")
    @GetMapping("/api/features/model-input")
    public ModelInputFeaturesResponse getModelInputFeatures() {
        return featureService.getModelInputFeatures();
    }
}