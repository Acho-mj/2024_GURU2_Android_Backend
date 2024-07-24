package guru2.team8.service.member.domain.dto;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserInfoDto {
    private String username;
    private String password;
    public String getUsername() {
        return username;
    }
}
