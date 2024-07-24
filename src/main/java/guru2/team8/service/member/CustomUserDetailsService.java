package guru2.team8.service.member;

import guru2.team8.service.member.domain.CustomUserDetails;
import guru2.team8.service.member.domain.Member;
import guru2.team8.service.member.domain.dto.CustomUserInfoDto;
import guru2.team8.service.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));

        CustomUserInfoDto customUserInfoDto = CustomUserInfoDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .build();

        return new CustomUserDetails(customUserInfoDto);
    }
}