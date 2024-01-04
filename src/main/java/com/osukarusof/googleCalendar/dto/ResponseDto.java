package com.osukarusof.googleCalendar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private Integer code;
    private String message;
    private Object data;
    private Map<String, Object> fieldErrors;
}
