package com.osukarusof.googleCalendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleCalendarDto {

    @NotNull(message = "This field is required")
    @JsonProperty("user_id")
    private Long userId;

    @NotNull(message = "This field is required")
    private String title;

    @NotNull(message = "This field is required")
    private String description;

    @NotNull(message = "This field is required")
    private String location;

    @NotNull(message = "This field is required")
    @JsonProperty("start_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startDateTime;

    @NotNull(message = "This field is required")
    @JsonProperty("end_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDateTime;
}
