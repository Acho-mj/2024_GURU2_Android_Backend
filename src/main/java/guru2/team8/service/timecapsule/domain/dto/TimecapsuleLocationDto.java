package guru2.team8.service.timecapsule.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimecapsuleLocationDto {
    private Long id;
    private String title;
    private String viewableAt;
    private Double latitude; // 위도
    private Double longitude; // 경도
    private long daysLeft; // 디데이

}
