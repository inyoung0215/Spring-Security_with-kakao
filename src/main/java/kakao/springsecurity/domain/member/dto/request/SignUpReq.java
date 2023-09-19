package kakao.springsecurity.domain.member.dto.request;

import kakao.springsecurity.global.common.customValid.valid.ValidNickName;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class SignUpReq {
    @Email
    private String email;

    @ValidNickName
    private String nickname;

    private String referralCode;

    @NotBlank
    private String providerId;
}
