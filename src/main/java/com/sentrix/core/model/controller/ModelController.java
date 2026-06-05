package com.sentrix.core.model.controller;

import com.sentrix.core.feature.dto.ModelInputFeaturesResponse;
import com.sentrix.core.feature.service.FeatureService;
import com.sentrix.core.model.client.ModelServerClient;
import com.sentrix.core.model.dto.ModelDiagnoseRequest;
import com.sentrix.core.model.dto.ModelDiagnoseResponse;
import com.sentrix.core.model.dto.ModelStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "model-controller", description = "Model Server 연동 API")
@RestController
public class ModelController {

    private final ModelServerClient modelServerClient;
    private final FeatureService featureService;

    public ModelController(
            ModelServerClient modelServerClient,
            FeatureService featureService
    ) {
        this.modelServerClient = modelServerClient;
        this.featureService = featureService;
    }

    @Operation(summary = "sentrix-model-server의 연결 상태를 확인한다.")
    @GetMapping("/api/model/status")
    public ModelStatusResponse getModelStatus() {
        return modelServerClient.getModelStatus();
    }

    @Operation(summary = "현재 model input feature를 이용해 model-server의 진단 API를 테스트 호출한다.")
    @PostMapping("/api/model/diagnose")
    public ModelDiagnoseResponse diagnose() {
        ModelInputFeaturesResponse modelInputFeatures = featureService.getModelInputFeatures();

        ModelDiagnoseRequest request = new ModelDiagnoseRequest(
                LocalDateTime.now(),
                modelInputFeatures.getSchemaVersion(),
                modelInputFeatures.getFeatures()
        );

        return modelServerClient.diagnose(request);
    }
}