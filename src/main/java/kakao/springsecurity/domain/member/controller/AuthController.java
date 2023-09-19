package kakao.springsecurity.domain.member.controller;

import kakao.springsecurity.domain.member.dto.request.SignUpReq;
import kakao.springsecurity.domain.member.dto.response.*;
import kakao.springsecurity.domain.member.service.AuthService;
import kakao.springsecurity.global.security.jwt.JwtTokenProvider;
import kakao.springsecurity.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    /* 토큰 재발급*/
    @GetMapping("/reissue")
    public ResponseEntity<ReissueResp> reissue(
            @RequestHeader(JwtTokenProvider.HEADER) String accessToken,
            @CookieValue(CookieUtil.NAME_REFRESH_TOKEN) String refreshToken
    ) {
        accessToken = jwtTokenProvider.resolveToken(accessToken);
        ReissueResp response = authService.reissue(accessToken, refreshToken);
        HttpHeaders headers = getCookieHeaders(response.getRefreshToken());
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<KakaoLoginResp> kakaoLogin(
            @RequestParam String code
    ) {
        KakaoLoginResp response = authService.kakaoLogin(code);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResp> signup(
            @RequestBody SignUpReq request,
            Errors errors
    ) {
        SignUpResp response = authService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/email")
    public ResponseEntity<ExistEmailResp> checkEmail(
            @RequestParam @Email String email
    ) {
        ExistEmailResp response = authService.checkEmail(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/referral")
    public ResponseEntity<ExistReferralCodeResp> checkReferralCode(
            @RequestParam @NotBlank String referralCode
    ) {
        ExistReferralCodeResp response = authService.checkReferralCode(referralCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private HttpHeaders getCookieHeaders(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = CookieUtil.getRefreshTokenCookie(refreshToken);
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return headers;
    }
}
