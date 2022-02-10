package kr.sproutfx.oauth.authorization.api.member.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.sproutfx.oauth.authorization.api.member.exception.MemberNotFoundException;
import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.repository.MemberRepository;
import kr.sproutfx.oauth.authorization.api.member.repository.specification.MemberSpecification;

@Service
@Transactional(readOnly = true)
public class MemberService {
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;
    SimpleDateFormat simpleDateFormat;
    
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, SimpleDateFormat simpleDateFormat) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.simpleDateFormat = simpleDateFormat;
    }

    public List<Member> findAll() {
        return this.memberRepository.findAll();
    }

    public Member findById(UUID id) {
        return this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public Member findByEmail(String email) {
        return this.memberRepository.findOne(MemberSpecification.equalEmail(email)).orElseThrow(MemberNotFoundException::new);
    }

    @Transactional
    public UUID create(String email, String name, String password, String description) {
        Member persistenceMember = this.memberRepository.save(
            Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .passwordExpired(simpleDateFormat.format(System.currentTimeMillis()))
                .status(MemberStatus.PENDING_APPROVAL)
                .description(description)
                .build());

        return persistenceMember.getId();
    }

    @Transactional
    public void update(UUID id, String email, String name, String description) {
        Member persistenceMember = this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        persistenceMember.setEmail(email);
        persistenceMember.setName(name);
        persistenceMember.setDescription(description);
    }

    @Transactional
    public void updateStatus(UUID id, MemberStatus memberStatus) {
        Member persistenceMember = this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        persistenceMember.setStatus(memberStatus);
    }

    @Transactional
    public void deleteById(UUID id) {
        Member persistenceMember = this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        this.memberRepository.delete(persistenceMember);
    }

}
