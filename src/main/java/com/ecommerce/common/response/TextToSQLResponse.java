package com.ecommerce.common.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TextToSQLResponse {
    @JsonProperty("status")
    private String status;  // "success" / "error"

    @JsonProperty("result")
    private Object result;  // FastAPI result 객체

    @JsonProperty("error")
    private String error;   // 에러 메시지
}
