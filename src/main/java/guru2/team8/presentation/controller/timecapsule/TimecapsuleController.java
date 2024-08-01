package guru2.team8.presentation.controller.timecapsule;


import guru2.team8.service.timecapsule.S3Service;
import guru2.team8.service.timecapsule.CapsuleLocationService;
import guru2.team8.service.timecapsule.TimecapsuleService;
import guru2.team8.service.timecapsule.domain.Timecapsule;
import guru2.team8.service.timecapsule.domain.dto.TimecapsuleDto;
import guru2.team8.service.timecapsule.domain.dto.TimecapsuleLocationDto;
import guru2.team8.service.timecapsule.domain.dto.TimecapsuleReqDto;
import guru2.team8.service.timecapsule.domain.dto.TimecapsuleUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timecapsules")
public class TimecapsuleController {

    private final S3Service s3Service;
    private final TimecapsuleService timecapsuleService;
    private final CapsuleLocationService capsuleLocationService;

    // 타임캡슐 작성
    @PostMapping("/")
    public ResponseEntity<String> newTimeCapsule(
            @RequestPart(value = "timecapsuleReqDto") TimecapsuleReqDto timecapsuleReqDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile){

        String fileName = "";
        if (imageFile != null && !imageFile.isEmpty()) {
            // 이미지 업로드
            try {
                // S3 버킷의 timecapsule 디렉토리 안에 저장됨
                fileName = s3Service.upload(imageFile, "timecapsule");
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 중 오류가 발생했습니다.");
            }
        }

        // 타임캡슐 작성
        try{
            TimecapsuleReqDto response = timecapsuleService.newTimeCapsule(timecapsuleReqDto, fileName);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("타임캡슐 작성 실패");
        }
    }
    
    // 타입캡슐 열람가능
    @GetMapping("/view")
    public ResponseEntity<List<TimecapsuleDto>> viewTimecapsules(@RequestParam("category") String category) {
        List<TimecapsuleDto> timecapsules = timecapsuleService.getViewableTimecapsules(category);
        return ResponseEntity.ok(timecapsules);
    }

    // 타임캡슐 열람불가능
    @GetMapping("/unviewable")
    public ResponseEntity<List<TimecapsuleDto>> unViewableTimecapsules(@RequestParam("category") String category) {
        List<TimecapsuleDto> timecapsules = timecapsuleService.getUnViewableTimecapsules(category);
        return ResponseEntity.ok(timecapsules);
    }

    // 열람가능한 타임캡슐 상세조회
    @GetMapping("/viewable/{id}")
    public ResponseEntity<TimecapsuleDto> getDetailTimecapsule(@PathVariable("id") Long id){
        try{
            TimecapsuleDto timecapsuleDto = timecapsuleService.getDetailTimecapsule(id);
            return ResponseEntity.ok(timecapsuleDto);

        }catch (Exception e) {
            return new ResponseEntity("타임캡슐 없음", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 홈화면에서 타임캡슐 조회
    @GetMapping("/main/{id}")
    public ResponseEntity<TimecapsuleLocationDto> getTimecapsule(@PathVariable("id") Long id){
        try{
            TimecapsuleLocationDto timecapsuleLocationDto = timecapsuleService.getTimecapsule(id);
            return ResponseEntity.ok(timecapsuleLocationDto);

        }catch (Exception e) {
            return new ResponseEntity("타임캡슐 없음", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 타임캡슐 수정
    @PutMapping("/{id}")
    public ResponseEntity<TimecapsuleDto> updateTimeCapsule(
            @PathVariable("id") Long id,
            @RequestPart(value = "timecapsuleUpdateDto") TimecapsuleUpdateDto timecapsuleUpdateDto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        String fileName = "";
        if (imageFile != null && !imageFile.isEmpty()) {
            // 이미지 업로드
            try {
                // S3 버킷의 timecapsule 디렉토리 안에 저장됨
                fileName = s3Service.upload(imageFile, "timecapsule");
            } catch (IOException e) {
                return new ResponseEntity("이미지 업로드 중 에러", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try {
            TimecapsuleDto updatedTimecapsule = timecapsuleService.updateTimeCapsule(id, timecapsuleUpdateDto, fileName);
            return ResponseEntity.ok(updatedTimecapsule);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
