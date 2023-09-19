package kakao.springsecurity.domain.member.dto.response;

import lombok.Getter;

@Getter
public class ExistReferralCodeResp {
    private final String referralCode;
    private final Boolean isExists;

    public ExistReferralCodeResp(String referralCode, Boolean isExists) {
        this.referralCode = referralCode;
        this.isExists = isExists;
    }
}
