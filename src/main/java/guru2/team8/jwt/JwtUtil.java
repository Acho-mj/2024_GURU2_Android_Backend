package guru2.team8.jwt;

import guru2.team8.service.member.domain.dto.CustomUserInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean {

    @Value("${jwt.token.key}")
    private String SECRET_KEY;
    private final long accessTokenValidTime = (60 * 1000) * 30; // 30분
    private Key key;

    @Override
    public void afterPropertiesSet() {
        try {
            byte[] keyBytes = java.util.Base64.getDecoder().decode(SECRET_KEY);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid BASE64 encoding of SECRET_KEY", e);
        }
    }

    // Access Token 생성
    public String createAccessToken(CustomUserInfoDto customUserInfoDto) {
        return createToken(customUserInfoDto, accessTokenValidTime);
    }

    // JWT 토큰 생성
    public String createToken(CustomUserInfoDto customUserInfoDto, long accessTokenValidTime) {
        Claims claims = Jwts.claims();
        claims.put("username", customUserInfoDto.getUsername());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenValidTime);

        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant())) // set Expire Time 해당 옵션 안넣으면 expire 안함
                .signWith(key, SignatureAlgorithm.HS256) // 사용할 암호화 알고리즘과 , signature 에 들어갈 secret값 세팅
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
