package kakao.springsecurity.domain.member.service;

import kakao.springsecurity.domain.member.dto.request.SignUpReq;
import kakao.springsecurity.domain.member.dto.response.*;
import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.domain.member.repository.MemberRepository;
import kakao.springsecurity.global.error.ErrorMessage;
import kakao.springsecurity.global.error.exception.Exception400;
import kakao.springsecurity.global.error.exception.Exception404;
import kakao.springsecurity.global.redis.RedisService;
import kakao.springsecurity.global.security.jwt.JwtTokenProvider;
import kakao.springsecurity.global.security.oauth.KakaoUserInfo;
import kakao.springsecurity.global.security.oauth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URI;
    private static final Set<String> existingCodes = new HashSet<>();
    private static final String UPPER_CASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";

    private final InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public KakaoLoginResp kakaoLogin(String code) {
        ClientRegistration kakaoProvider = inMemoryClientRegistrationRepository.findByRegistrationId("kakao");
        OAuth2AccessTokenResponse tokenResponse = getToken(code, kakaoProvider);
        Map<String, Object> userAttributes = getUserAttributes(tokenResponse);
        OAuth2UserInfo oauth2UserInfo = new KakaoUserInfo(userAttributes);

        String email = oauth2UserInfo.getEmail();
        String providerId = oauth2UserInfo.getProviderId();

        Optional<Member> optionalMember = memberRepository.findByEmailAndProviderId(email, providerId);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            String accessToken = jwtTokenProvider.createAccessToken(member);
            String refreshToken = jwtTokenProvider.createRefreshToken(member);

            // 리프레시 토큰  Redis에 저장 ( key = "RT " + Email / value = refreshToken )
            redisService.setObjectByKey(RedisService.REFRESH_TOKEN_PREFIX + member.getEmail(), refreshToken,
                    JwtTokenProvider.EXP_REFRESH, TimeUnit.MILLISECONDS);

            return new KakaoLoginResp(member, accessToken, refreshToken);
        }

        return new KakaoLoginResp(providerId, email);
    }

    private OAuth2AccessTokenResponse getToken(String code, ClientRegistration kakaoProvider) {
        return WebClient.create()
                .post()
                .uri(kakaoProvider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, kakaoProvider))
                .retrieve()
                .bodyToMono(OAuth2AccessTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    private Map<String, Object> getUserAttributes(OAuth2AccessTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(KAKAO_USER_INFO_URI)
                .headers(header -> header.setBearerAuth(String.valueOf(tokenResponse.getAccessToken())))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @Transactional
    public SignUpResp signUp(SignUpReq request) {
        String email = request.getEmail();
        String nickname = request.getNickname();
        String invitedCode = request.getReferralCode();
        String providerId = request.getProviderId();

        if (memberRepository.existsByEmail(email)) {
            throw new Exception400("email", ErrorMessage.DUPLICATED_EMAIL);
        }

        if (memberRepository.existsByNickname(nickname)) {
            throw new Exception400("nickname", ErrorMessage.DUPLICATED_NICKNAME);
        }

        if (invitedCode != null && !invitedCode.isEmpty()) {
            Member inviteMember = memberRepository.findByReferralCode(invitedCode)
                    .orElseThrow(() -> new Exception404(ErrorMessage.REFERRAL_CODE_NOT_FOUND));

            // 포인트 2000점씩 획득, 현재 포인트 엔티티 이해x.. 양방향..?

        }

        String referralCode = generateRandomCode();
        Member member = Member.createMember(email, nickname, referralCode, providerId);
        memberRepository.save(member);

        return new SignUpResp(member);
    }

    @Transactional(readOnly = true)
    public ExistEmailResp checkEmail(String email) {
        Boolean isExists = memberRepository.existsByEmail(email);
        return new ExistEmailResp(email, isExists);
    }

    @Transactional(readOnly = true)
    public ExistReferralCodeResp checkReferralCode(String referralCode) {
        Boolean isExists = memberRepository.existsByReferralCode(referralCode);
        return new ExistReferralCodeResp(referralCode, isExists);
    }

    private String generateRandomCode() {
        List<Character> characters = new ArrayList<>();
        Random random = new Random();
        String uniqueCode;

        do {
            for (int i = 0; i < 5; i++) {
                int randomIndex = random.nextInt(UPPER_CASE_CHARACTERS.length());
                char randomChar = UPPER_CASE_CHARACTERS.charAt(randomIndex);
                characters.add(randomChar);
            }

            for (int i = 0; i < 3; i++) {
                int randomIndex = random.nextInt(DIGITS.length());
                char randomChar = DIGITS.charAt(randomIndex);
                characters.add(randomChar);
            }

            Collections.shuffle(characters);

            StringBuilder codeBuilder = new StringBuilder(characters.size());
            for (Character character : characters) {
                codeBuilder.append(character);
            }

            uniqueCode = codeBuilder.toString();
        } while (existingCodes.contains(uniqueCode));

        existingCodes.add(uniqueCode);

        return uniqueCode;
    }

    public ReissueResp reissue(String accessToken, String refreshToken) {
        jwtTokenProvider.isTokenValid(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String redisRT = redisService.getObjectByKey(RedisService.REFRESH_TOKEN_PREFIX
                + authentication.getName(), String.class);

        if (!redisRT.equals(refreshToken)) {
            throw new Exception400("Refresh Token", "정보가 일치하지 않습니다.");
        }

        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new Exception400("email", ErrorMessage.USER_NOT_FOUND));

        String newAT = jwtTokenProvider.createAccessToken(member);
        String newRT = jwtTokenProvider.createRefreshToken(member);

        redisService.setObjectByKey(RedisService.REFRESH_TOKEN_PREFIX + member.getEmail(), newRT,
                JwtTokenProvider.EXP_REFRESH, TimeUnit.MILLISECONDS);

        return new ReissueResp(newAT, newRT);
    }
}
