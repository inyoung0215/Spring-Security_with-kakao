package kakao.springsecurity.domain.petTalk.entity;

import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.domain.petTalk.type.PetTalkStatus;
import kakao.springsecurity.domain.petTalk.type.PetType;
import kakao.springsecurity.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static kakao.springsecurity.domain.petTalk.type.PetTalkStatus.ACTIVE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pet_talk")
public class PetTalk extends BaseTimeEntity {

    @Id
    @Column(name = "pet_talk_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;
    
    @Column(name = "pet_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Builder.Default
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetTalkStatus status = ACTIVE;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @OneToMany(mappedBy = "petTalk", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<PetTalkPhoto> petTalkPhotos = new ArrayList<>();

    @Transient
    private String thumbnail;

    @Transient
    private boolean reacted; // 로그인된 사용자 이모지 반응 boolean 값

    @Transient
    private Long emojiCount;

    @Transient
    private Long replyCount;

    public void react(int i) {
        this.emojiCount += i;
    }

    public void uploadThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void uploadPetTalkPhotos(List<PetTalkPhoto> petTalkPhotos) {
        this.petTalkPhotos = petTalkPhotos;
    }

}
