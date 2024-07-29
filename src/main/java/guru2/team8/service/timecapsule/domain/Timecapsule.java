package guru2.team8.service.timecapsule.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timecapsule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="timecapsule_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;
    private String username;
    private String title; // 제목
    private String content; // 내용
    private String fileName; // 사진 업로드 URL
    private String category; // 카테고리

    private LocalDateTime createdAt; // 작성 날짜

    private LocalDateTime updatedAt; // 수정 날짜

    private String viewableAt; // 열람 가능한 날짜

}
