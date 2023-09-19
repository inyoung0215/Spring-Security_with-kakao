package kakao.springsecurity.domain.petTalk.entity;

import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.domain.petTalk.type.PetGender;
import kakao.springsecurity.domain.petTalk.type.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType species;

    @Column(nullable = false)
    private String petName;

    private String image;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetGender petGender;

    @Column(nullable = false)
    private Integer petAge;


}
