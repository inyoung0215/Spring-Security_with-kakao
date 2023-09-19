package kakao.springsecurity.domain.petTalk.entity;

import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet_talk_reply")
@Entity
public class PetTalkReply extends BaseTimeEntity {
    @Id
    @Column(name = "pet_talk_reply_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_talk_id")
    private PetTalk petTalk;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private PetTalkReply parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<PetTalkReply> children = new ArrayList<>();

    public static PetTalkReply create(Member writer, PetTalk petTalk, String content) {
        return PetTalkReply.builder()
                .writer(writer)
                .petTalk(petTalk)
                .content(content)
                .build();
    }

    public void updateParent(PetTalkReply parent) {
        this.parent = parent;
    }
}