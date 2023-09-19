package kakao.springsecurity.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.global.security.auth.PrincipalDetails;
import kakao.springsecurity.global.security.auth.PrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    public static final Long EXP_ACCESS = 1000L * 60 * 30; // 30분
    public static final Long EXP_REFRESH = 1000L * 60 * 60 * 24 * 14; // 14일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    private final PrincipalDetailsService principalDetailsService;

    private Key JWT_KEY;

    @Autowired
    public JwtTokenProvider(@Value("${jwt.key}") String secretKey, PrincipalDetailsService principalDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        JWT_KEY = Keys.hmacShaKeyFor(keyBytes);

        this.principalDetailsService = principalDetailsService;
    }

    public String createAccessToken(Member member) {
        String jwt = Jwts.builder()
                .setSubject(member.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + EXP_ACCESS))
                .claim("id", member.getId())
                .claim("role", String.valueOf(member.getRole()))
                .signWith(JWT_KEY, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public String createRefreshToken(Member member) {
        String jwt = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + EXP_REFRESH))
                .signWith(JWT_KEY, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public String resolveToken(String header) {
        return header.replaceAll(TOKEN_PREFIX, "");
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(JWT_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new JwtException("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new JwtException("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new JwtException("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new JwtException("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new JwtException("JWT claims string is empty.");
        }
    }

    public Authentication getAuthentication(String jwt) {
        PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(getEmail(jwt));
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    public String getEmail(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public Long getExpiration(String jwt) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(JWT_KEY)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getExpiration();

        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
