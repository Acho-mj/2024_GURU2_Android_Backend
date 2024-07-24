package guru2.team8.service.timecapsule.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 타임캡슐 작성 시 요청
public class TimecapsuleReqDto {

    private String title; // 제목
    private String content; // 내용
    private String category; // 카테고리
    private String fileName;
    private LocalDateTime viewableAt;  // 열람가능날짜

    private Double latitude; // 위도
    private Double longitude; // 경도

}
