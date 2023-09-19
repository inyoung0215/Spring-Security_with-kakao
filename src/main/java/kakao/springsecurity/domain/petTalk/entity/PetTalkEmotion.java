package kakao.springsecurity.domain.petTalk.entity;

import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.domain.petTalk.type.EmojiType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_talk_emotion")
@Entity
public class PetTalkEmotion {

    @Id
    @Column(name = "pet_talk_emotion_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_talk_id")
    private PetTalk petTalk;

    @Column(name = "emoji", nullable = false)
    @Enumerated(EnumType.STRING)
    EmojiType emoji;

    public static PetTalkEmotion create(Member member, PetTalk petTalk, EmojiType emoji) {
        return PetTalkEmotion.builder()
                .member(member)
                .petTalk(petTalk)
                .emoji(emoji)
                .build();
    }

}
