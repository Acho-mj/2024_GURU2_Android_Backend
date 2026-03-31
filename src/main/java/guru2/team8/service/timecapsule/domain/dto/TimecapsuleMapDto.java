package guru2.team8.service.timecapsule.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimecapsuleMapDto {
    private Long id;
    private Long memberId;
    private String username;
    private String title;
    private String category;
    private String viewableAt;
    private Double latitude;
    private Double longitude;
    private long daysLeft;
    private Double distanceMeters;
}

