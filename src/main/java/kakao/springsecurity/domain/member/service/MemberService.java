package kakao.springsecurity.domain.member.service;

import kakao.springsecurity.domain.member.dto.request.SavePetReq;
import kakao.springsecurity.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Transactional
    public void savePetInfo(SavePetReq request, Member member) {

    }
}
