package guru2.team8.service.member;


import guru2.team8.jwt.JwtUtil;
import guru2.team8.service.member.domain.Member;
import guru2.team8.service.member.domain.dto.MemberReqDto;
import guru2.team8.service.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
        Optional<Member> member = memberRepository.findByUsername(memberReqDto.getUsername());
        if (member.isPresent() && passwordEncoder.matches(memberReqDto.getPassword(), member.get().getPassword())) {
            return jwtUtil.generateToken(member.get().getUsername());
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
}
