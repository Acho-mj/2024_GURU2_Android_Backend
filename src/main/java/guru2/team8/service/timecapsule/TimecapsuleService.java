package guru2.team8.service.timecapsule;

import guru2.team8.service.member.MemberService;
import guru2.team8.service.member.domain.dto.MemberResDto;
import guru2.team8.service.timecapsule.domain.CapsuleLocation;
import guru2.team8.service.timecapsule.domain.Timecapsule;
import guru2.team8.service.timecapsule.domain.dto.TimecapsuleDto;
import guru2.team8.service.timecapsule.domain.dto.TimecapsuleReqDto;
import guru2.team8.service.timecapsule.repository.CapsuleLocationRepository;
import guru2.team8.service.timecapsule.repository.TimecapsuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TimecapsuleService {
    private final MemberService memberService;
    private final TimecapsuleRepository timecapsuleRepository;
    private final CapsuleLocationRepository capsuleLocationRepository;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 타임캡슐 작성
    public TimecapsuleReqDto newTimeCapsule(
            TimecapsuleReqDto timecapsuleReqDto,
            String fileName){

        // 현재 로그인한 멤버 가져오기
        MemberResDto memberResDto = memberService.getMemberInfo();

        // 타임캡슐 엔티티 생성
        Timecapsule timecapsule = Timecapsule.builder()
                .memberId(memberResDto.getId())
                .username(memberResDto.getUsername())
                .title(timecapsuleReqDto.getTitle())
                .content(timecapsuleReqDto.getContent())
                .fileName(fileName)
                .category(timecapsuleReqDto.getCategory())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .viewableAt(timecapsuleReqDto.getViewableAt())
                .build();

        // 타임캡슐 저장
        Timecapsule savedTimecapsule = timecapsuleRepository.save(timecapsule);

        // 캡슐 위치 엔티티 생성
        CapsuleLocation capsuleLocation = CapsuleLocation.builder()
                .timeCapsuleId(savedTimecapsule.getId())
                .latitude(timecapsuleReqDto.getLatitude())
                .longitude(timecapsuleReqDto.getLongitude())
                .build();

        // 캡슐 위치 저장
        capsuleLocationRepository.save(capsuleLocation);

        TimecapsuleReqDto timecapsuleRes = TimecapsuleReqDto.builder()
                .title(timecapsule.getTitle())
                .content(timecapsule.getContent())
                .category(timecapsule.getCategory())
                .fileName(timecapsule.getFileName())
                .viewableAt(timecapsule.getViewableAt())
                .latitude(capsuleLocation.getLatitude())
                .longitude(capsuleLocation.getLongitude())
                .build();

        return timecapsuleRes;
    }

    // 타임캡슐 열람가능
    public List<TimecapsuleDto> getViewableTimecapsules(String category) {
        List<Timecapsule> timecapsules = timecapsuleRepository.findAll();

        return timecapsules.stream()
                .filter(timecapsule -> LocalDateTime.now().isAfter(LocalDateTime.parse(timecapsule.getViewableAt())))
                .filter(timecapsule -> "전체".equals(category) || category.equals(timecapsule.getCategory()))
                .map(timecapsule -> new TimecapsuleDto(
                        timecapsule.getId(),
                        timecapsule.getTitle(),
                        timecapsule.getContent(),
                        timecapsule.getCategory(),
                        timecapsule.getFileName(),
                        timecapsule.getViewableAt()
                ))
                .collect(Collectors.toList());
    }

    // 타임캡슐 열람불가능
    public List<TimecapsuleDto> getUnViewableTimecapsules(String category) {
        List<Timecapsule> timecapsules = timecapsuleRepository.findAll();

        return timecapsules.stream()
                .filter(timecapsule -> LocalDateTime.now().isBefore(LocalDateTime.parse(timecapsule.getViewableAt())))
                .filter(timecapsule -> "전체".equals(category) || category.equals(timecapsule.getCategory()))
                .map(timecapsule -> {
                    long daysLeft = ChronoUnit.DAYS.between(LocalDateTime.now(), LocalDateTime.parse(timecapsule.getViewableAt()));
                    return new TimecapsuleDto(
                            timecapsule.getId(),
                            timecapsule.getTitle(),
                            null,
                            timecapsule.getCategory(),
                            null,
                            "D-" + daysLeft
                    );
                })
                .collect(Collectors.toList());
    }
    
    // 열람가능한 타임캡슐 상세조회
    public TimecapsuleDto getDetailTimecapsule(Long id) {
        // 타임캡슐 조회
        Optional<Timecapsule> optionalTimecapsule = timecapsuleRepository.findById(id);

        if (optionalTimecapsule.isPresent()) {
            Timecapsule timecapsule = optionalTimecapsule.get();

            // viewableAt을 LocalDate로 변환
            String viewableDate = LocalDateTime.parse(timecapsule.getViewableAt()).toLocalDate().format(dateFormatter);

            // 열람 가능 여부 체크
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(timecapsule.getViewableAt()))) {
                return new TimecapsuleDto(
                        timecapsule.getId(),
                        timecapsule.getTitle(),
                        timecapsule.getContent(),
                        timecapsule.getCategory(),
                        timecapsule.getFileName(),
                        viewableDate
                );
            } else {
                // 열람 불가능한 경우에도 타임캡슐의 제목과 날짜를 반환할 수 있음
                return new TimecapsuleDto(
                        timecapsule.getId(),
                        timecapsule.getTitle(),
                        null,
                        null,
                        null,
                        viewableDate
                );
            }
        } else {
            throw new RuntimeException("타임캡슐 없음");
        }
    }
}
