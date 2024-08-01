package guru2.team8.service.timecapsule.domain.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimecapsuleDto {
    private String title;
    private String content;
    private String category;
    private String fileName;
    private String viewableAt;
}

