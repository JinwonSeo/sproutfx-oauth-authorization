package kr.sproutfx.oauth.authorization.api.member.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.sproutfx.oauth.authorization.api.member.model.dto.MemberCreate;
import kr.sproutfx.oauth.authorization.api.member.model.dto.MemberUpdate;
import kr.sproutfx.oauth.authorization.api.member.model.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.model.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;
import kr.sproutfx.oauth.authorization.common.model.dto.Response;

@RestController
@RequestMapping("/members")
public class MemberController {
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public Response<List<Member>> findAll() {
        return new Response<>(this.memberService.findAll());
    }

    @GetMapping("/{id}")
    public Response<Member> findById(@PathVariable UUID id) {
        return new Response<>(this.memberService.findById(id));
    }

    @PostMapping
    public Response<Member> create(@RequestBody MemberCreate memberCreate) {
        return new Response<>(this.memberService.create(memberCreate));
    }

    @PutMapping("/{id}")
    public Response<Member> update(@PathVariable UUID id, @RequestBody MemberUpdate memberUpdate) {
        return new Response<>(this.memberService.update(id, memberUpdate));
    }

    @PutMapping("/{id}/status")
    public Response<Member> updateStatus(@PathVariable UUID id, @RequestBody MemberStatus memberStatus) {
        return new Response<>(this.memberService.updateStatus(id, memberStatus));
    }

    @DeleteMapping("/{id}")
    public Response<Member> delete(@PathVariable UUID id) {
        return new Response<>(this.memberService.deleteById(id));
    }
}
