package guru2.team8.presentation.controller.member;


import guru2.team8.service.member.MemberService;
import guru2.team8.service.member.domain.Member;
import guru2.team8.service.member.domain.dto.MemberReqDto;
import guru2.team8.service.member.domain.dto.MemberResDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    // 로그인 엔드포인트 - 성공 시 JWT 반환
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberReqDto memberReqDto) {
        String token = memberService.login(memberReqDto);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
    }

    // 회원가입 엔드포인트
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody MemberReqDto memberReqDto) {
        if (memberService.signup(memberReqDto)) {
            return ResponseEntity.ok("회원가입 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 아이디");
    }

    // 현재 로그인한 회원 정보 조회
    @GetMapping("/user")
    public ResponseEntity<MemberResDto> getMyUserInfo() {
        try {
            MemberResDto memberResDto = memberService.getMemberInfo();
            return new ResponseEntity(memberResDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("현재 로그인한 사용자의 회원정보를 가져올 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
