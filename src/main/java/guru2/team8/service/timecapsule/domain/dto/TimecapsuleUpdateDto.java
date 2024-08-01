package guru2.team8.service.timecapsule.domain.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimecapsuleUpdateDto {
    private String title; // 제목
    private String content; // 내용
    private String fileName; // 사진 업로드 URL
}
