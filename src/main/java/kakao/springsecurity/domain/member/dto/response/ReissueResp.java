package kakao.springsecurity.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class ReissueResp {
    private final String accessToken;

    @JsonIgnore
    private final String refreshToken;

    public ReissueResp(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
