package kakao.springsecurity.domain.member.dto.response;

import kakao.springsecurity.domain.member.entity.Member;
import lombok.Getter;

@Getter
public class SignUpResp {
    private final Long id;
    private final String email;
    private final String nickName;

    public SignUpResp(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickName = member.getNickname();
    }
}
