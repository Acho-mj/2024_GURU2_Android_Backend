package guru2.team8.service.timecapsule.domain.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimecapsuleDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String fileName;
    private String viewableAt;
}

