package guru2.team8.service.member;


import guru2.team8.jwt.JwtUtil;
import guru2.team8.service.member.domain.Member;
import guru2.team8.service.member.domain.dto.CustomUserInfoDto;
import guru2.team8.service.member.domain.dto.MemberReqDto;
import guru2.team8.service.member.domain.dto.MemberResDto;
import guru2.team8.service.member.repository.MemberRepository;
import guru2.team8.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 로그인 - 성공 시 JWT 반환
    public String login(MemberReqDto memberReqDto) {
        String username = memberReqDto.getUsername();
        String password = memberReqDto.getPassword();
        Optional<Member> optionalMember = memberRepository.findByUsername(memberReqDto.getUsername());
        Member member = optionalMember.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        if (passwordEncoder.matches(password, member.getPassword())) {
            // CustomUserInfoDto 객체 생성
            CustomUserInfoDto customUserInfoDto = CustomUserInfoDto.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .build();

            // JWT 토큰 생성
            return jwtUtil.createAccessToken(customUserInfoDto); // 30분 유효 기간을 가진 토큰 생성
        }
        return null;
    }



    // 회원가입
    public boolean signup(MemberReqDto memberReqDto) {
        if (memberRepository.findByUsername(memberReqDto.getUsername()).isPresent()) {
            return false; // 이미 존재하는 사용자 이름
        }
        Member member = new Member();
        member.setUsername(memberReqDto.getUsername());
        member.setPassword(passwordEncoder.encode(memberReqDto.getPassword())); // 비밀번호 암호화
        memberRepository.save(member);
        return true;
    }

    // 현재 로그인한 멤버 정보 조회
    public MemberResDto getMemberInfo() {
        // 토큰에서 사용자 이름 추출
        String username = SecurityUtil.getCurrentMember();

        // 사용자 이름으로 멤버 조회
        Optional<Member> currentMember = memberRepository.findByUsername(username);

        // 조회한 회원이 존재한다면
        if (currentMember.isPresent()) {
            Member member = currentMember.get();

            // entity -> dto 변환
            return new MemberResDto(member.getId(), member.getUsername());

        } else {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
    }
}
