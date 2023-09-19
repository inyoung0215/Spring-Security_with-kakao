package kakao.springsecurity.domain.member.dto.response;

import lombok.Getter;

@Getter
public class ExistEmailResp {
    private final String email;
    private final Boolean isExists;

    public ExistEmailResp(String email, Boolean isExists) {
        this.email = email;
        this.isExists = isExists;
    }
}
