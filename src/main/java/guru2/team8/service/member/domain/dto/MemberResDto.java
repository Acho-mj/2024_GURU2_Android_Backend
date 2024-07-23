package guru2.team8.service.member.domain.dto;

import guru2.team8.service.member.domain.Member;
import lombok.Getter;

@Getter
public class MemberResDto {
    private Long id;
    private String username;

    public MemberResDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
