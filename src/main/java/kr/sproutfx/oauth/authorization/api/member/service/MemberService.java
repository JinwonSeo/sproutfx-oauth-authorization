package kr.sproutfx.oauth.authorization.api.member.service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.sproutfx.oauth.authorization.api.member.exception.MemberNotFoundException;
import kr.sproutfx.oauth.authorization.api.member.model.dto.MemberCreate;
import kr.sproutfx.oauth.authorization.api.member.model.dto.MemberUpdate;
import kr.sproutfx.oauth.authorization.api.member.model.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.model.enumeration.MemberStatus;
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
    public Member create(MemberCreate memberCreate) {
        return this.memberRepository.save(Member.builder()
            .email(memberCreate.getEmail())
            .name(memberCreate.getName())
            .password(memberCreate.getPassword())
            .passwordExpired(simpleDateFormat.format(System.currentTimeMillis()))
            .status(MemberStatus.PENDING_APPROVAL)
            .build());
    }

    @Transactional
    public Member update(UUID id, MemberUpdate memberUpdate) {
        Member persistenceMember = this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        persistenceMember.setEmail(memberUpdate.getEmail());
        persistenceMember.setName(memberUpdate.getName());
        persistenceMember.setDescription(memberUpdate.getDescription());

        return persistenceMember;
    }

    @Transactional
    public Member updateStatus(UUID id, MemberStatus memberStatus) {
        Member persistenceMember = this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        persistenceMember.setStatus(memberStatus);

        return persistenceMember;
    }

    @Transactional
    public Member deleteById(UUID id) {
        Member persistenceMember = this.memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);

        this.memberRepository.delete(persistenceMember);

        return persistenceMember;
    }
}
