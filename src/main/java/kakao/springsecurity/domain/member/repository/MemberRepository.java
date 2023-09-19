package kakao.springsecurity.domain.member.repository;

import kakao.springsecurity.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByEmailAndProviderId(String email, String providerId);

    boolean existsByReferralCode(String referralCode);

    Optional<Member> findByReferralCode(String referralCode);
}
