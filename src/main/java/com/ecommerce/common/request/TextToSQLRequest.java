package com.ecommerce.common.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // Jackson 기본 생성자
@AllArgsConstructor
public class TextToSQLRequest {
    private String query;
}
