package com.ecommerce.common.text2sql;

import com.ecommerce.common.request.TextToSQLRequest;
import com.ecommerce.common.response.TextToSQLResponse;
import com.ecommerce.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/text2sql")
@Slf4j
public class TextToSQLController {

    private final RestTemplate restTemplate;
    private final String fastApiUrl = "http://host.docker.internal:8000/fastApi";

    public TextToSQLController() {
        this.restTemplate = new RestTemplate();
    }

    //TODO: 백엔드에서 트랜잭션 관리하도록
    @PostMapping("/execute")
    public ResponseEntity<ApiResponse<TextToSQLResponse>> executeNaturalQuery(@RequestBody TextToSQLRequest request) {
        String query = request.getQuery();
        log.info("자연어 쿼리: {}", query);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TextToSQLRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<TextToSQLResponse> fastApiResponse = restTemplate.postForEntity(
                    fastApiUrl + "/execute",
                    entity,
                    TextToSQLResponse.class
            );

            log.info("FastAPI 응답: status={}, result={}", fastApiResponse.getStatusCode(), fastApiResponse.getBody());

            TextToSQLResponse result = fastApiResponse.getBody();
            ApiResponse<TextToSQLResponse> wrappedResponse = new ApiResponse<>(
                    true,
                    "Text2SQL 실행 성공",
                    result
            );

            return ResponseEntity.ok(wrappedResponse);

        } catch (Exception e) {
            log.error("FastAPI 연결 실패: {}", e.getMessage(), e);

            TextToSQLResponse errorResult = new TextToSQLResponse();
            errorResult.setError("FastAPI 서버 연결 불가: " + e.getMessage());

            ApiResponse<TextToSQLResponse> errorResponse = new ApiResponse<>(
                    false,
                    "FastAPI 연결 실패: " + e.getMessage(),
                    errorResult
            );

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }
}
