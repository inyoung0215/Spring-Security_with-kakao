package kakao.springsecurity.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kakao.springsecurity.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class KakaoLoginResp {
    private Long id;
    private String email;
    private String nickName;
    private String providerId;
    private String profileImageUrl;
    private String accessToken;

    @JsonIgnore
    private String refreshToken;

    public KakaoLoginResp(String providerId, String email) {
        this.providerId = providerId;
        this.email = email;
    }

    public KakaoLoginResp(Member member, String accessToken, String refreshToken) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickName = member.getNickname();
        this.profileImageUrl = member.getProfileImageUrl();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
