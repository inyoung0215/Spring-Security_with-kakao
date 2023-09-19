package kakao.springsecurity.global.error;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {
    public static final String LOGIN_FAILED = "회원 정보가 존재하지 않습니다.";
    public static final String UN_AUTHORIZED = "인증되지 않았습니다.";
    public static final String FORBIDDEN = "접근이 거부되었습니다.";
    public static final String USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    public static final String DUPLICATED_EMAIL = "이미 가입된 이메일입니다.";
    public static final String DUPLICATED_NICKNAME = "이미 사용중인 닉네임입니다.";
    public static final String REFERRAL_CODE_NOT_FOUND = "잘못된 추천인 코드입니다.";
}
