package kr.sproutfx.oauth.authorization.api.member.service;

import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.exception.MemberNotFoundException;
import kr.sproutfx.oauth.authorization.api.member.exception.MemberPasswordNotMatchesException;
import kr.sproutfx.oauth.authorization.api.member.repository.MemberRepository;
import kr.sproutfx.oauth.authorization.api.member.repository.specification.MemberSpecification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class MemberService {
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member findById(UUID id) {
        return this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public Member findByEmail(String email) {
        return this.memberRepository.findOne(MemberSpecification.equalEmail(email)).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public UUID updatePassword(String email, String currentPassword, String newPassword) {
        Member persistenceMember = this.memberRepository.findOne(MemberSpecification.equalEmail(email)).orElseThrow(MemberNotFoundException::new);

        if (!this.passwordEncoder.matches(currentPassword, persistenceMember.getPassword())) {
            throw new MemberPasswordNotMatchesException();
        }

        persistenceMember.setPassword(this.passwordEncoder.encode(newPassword));
        // To-do: password 90일 하드코딩 password 정책관리 기능 추가 후 변수화
        persistenceMember.setPasswordExpired(LocalDateTime.now().plusDays(90));

        return persistenceMember.getId();
    }
}
