package guru2.team8.service.timecapsule.domain;

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
public class CapsuleLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @Column(name = "timecapsule_id")
    private Long timeCapsuleId; // 타임캡슐 ID (Foreign Key)

    private Double latitude; // 위도
    private Double longitude; // 경도
}
