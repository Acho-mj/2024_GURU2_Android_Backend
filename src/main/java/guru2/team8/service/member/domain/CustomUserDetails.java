package guru2.team8.service.member.domain;

import guru2.team8.service.member.domain.dto.CustomUserInfoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final CustomUserInfoDto customUserInfoDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // role 없어서 빈
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return customUserInfoDto.getPassword();
    }


    @Override
    public String getUsername() {
        return customUserInfoDto.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
