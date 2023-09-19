package kakao.springsecurity.global.security.auth;

import kakao.springsecurity.domain.member.entity.Member;
import kakao.springsecurity.domain.member.repository.MemberRepository;
import kakao.springsecurity.global.error.ErrorMessage;
import kakao.springsecurity.global.error.exception.Exception404;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("시큐리티 로그인 시도 email: " + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new Exception404(ErrorMessage.USER_NOT_FOUND));

        return new PrincipalDetails(member);
    }
}
