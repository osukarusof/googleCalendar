package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleCalendar {

    @JsonProperty("user_id")
    private Long userId;

    private String title;

    private String description;

    @JsonProperty("start_date_time")
    private LocalDateTime startDateTime;

    @JsonProperty("end_date_time")
    private LocalDateTime endDateTime;
}
