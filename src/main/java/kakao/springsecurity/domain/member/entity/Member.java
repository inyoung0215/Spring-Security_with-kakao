package kakao.springsecurity.domain.member.entity;

import kakao.springsecurity.domain.member.type.MemberRole;
import kakao.springsecurity.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "provider_id", unique = true, nullable = false)
    private String providerId;

    @Builder.Default
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role = MemberRole.USER;

    @Column(name = "referral_code")
    private String referralCode;

    public static Member createMember(String email,String nickname, String referralCode, String providerId) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .referralCode(referralCode)
                .providerId(providerId)
                .build();
    }
}
