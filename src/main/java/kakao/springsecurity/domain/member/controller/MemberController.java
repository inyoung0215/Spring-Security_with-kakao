package kakao.springsecurity.domain.member.controller;

import kakao.springsecurity.domain.member.dto.request.SavePetReq;
import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.domain.member.service.MemberService;
import kakao.springsecurity.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/pet")
    public ResponseEntity<?> savePet(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody SavePetReq request
    ) {
        Member member = principalDetails.getMember();
        memberService.savePetInfo(request, member);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
