package kakao.springsecurity.domain.member.dto.request;

import kakao.springsecurity.domain.petTalk.type.PetGender;
import kakao.springsecurity.domain.petTalk.type.PetType;
import kakao.springsecurity.global.common.customValid.valid.ValidEnum;
import kakao.springsecurity.global.common.customValid.valid.ValidString;
import lombok.Getter;

@Getter
public class SavePetReq {
    @ValidEnum(enumClass = PetType.class)
    private PetType species;

    @ValidString
    private String petName;

    @ValidString
    private String breed;

    @ValidEnum(enumClass = PetGender.class)
    private PetGender petGender;

    private Integer petAge;
}
