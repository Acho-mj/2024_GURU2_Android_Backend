package guru2.team8.presentation.controller.timecapsule;


import guru2.team8.service.timecapsule.S3Service;
import guru2.team8.service.timecapsule.CapsuleLocationService;
import guru2.team8.service.timecapsule.TimecapsuleService;
import guru2.team8.service.timecapsule.domain.dto.TimecapsuleReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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



}
