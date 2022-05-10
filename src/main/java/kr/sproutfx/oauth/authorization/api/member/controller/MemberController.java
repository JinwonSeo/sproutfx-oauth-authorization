package kr.sproutfx.oauth.authorization.api.member.controller;

import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;
import kr.sproutfx.oauth.authorization.common.base.BaseController;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/members")
public class MemberController extends BaseController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<StructuredBody<List<MemberResponse>>> findAll() {

        return ResponseEntity.ok(StructuredBody.content(
            this.memberService.findAll().stream().map(MemberResponse::new).collect(toList())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StructuredBody<MemberResponse>> findById(@PathVariable UUID id) {

        Member selectedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.content(
            new MemberResponse(selectedMember)));
    }

    @PostMapping
    public ResponseEntity<StructuredBody<MemberResponse>> create(@RequestBody @Validated MemberCreateRequest memberCreateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        UUID id = this.memberService.create(memberCreateRequest.getEmail(), memberCreateRequest.getName(), memberCreateRequest.getPassword(), memberCreateRequest.getDescription());

        Member createdMember = this.memberService.findById(id);

        return ResponseEntity.created(URI.create(String.format("/members/%s", id))).body(StructuredBody.content(
            new MemberResponse(createdMember)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StructuredBody<MemberResponse>> update(@PathVariable UUID id, @RequestBody @Validated MemberUpdateRequest memberUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        String email = memberUpdateRequest.getEmail();
        String name = memberUpdateRequest.getName();
        String description = memberUpdateRequest.getDescription();

        this.memberService.update(id, email, name, description);

        Member updatedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.content(
            new MemberResponse(updatedMember)));
    }

    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<StructuredBody<MemberResponse>> updateStatus(@PathVariable UUID id, @RequestBody @Validated MemberStatusUpdateRequest memberStatusUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        MemberStatus memberStatus = memberStatusUpdateRequest.getMemberStatus();

        this.memberService.updateStatus(id, memberStatus);

        Member updatedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.content(
            new MemberResponse(updatedMember)));
    }

    @PatchMapping(value = "/{email}/password")
    public ResponseEntity<StructuredBody<MemberResponse>> updatePassword(@PathVariable String email, @RequestBody @Validated MemberPasswordUpdateRequest memberPasswordUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        String currentPassword = memberPasswordUpdateRequest.getCurrentPassword();
        String newPassword = memberPasswordUpdateRequest.getNewPassword();

        UUID id = this.memberService.updatePassword(email, currentPassword, newPassword);

        Member updatedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.content(
            new MemberResponse(updatedMember)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {

        this.memberService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Data
    static class MemberCreateRequest {
        @Email
        private String email;
        @NotBlank
        private String name;
        @NotBlank
        private String password;
        private String description;
    }

    @Data
    static class MemberUpdateRequest {
        @Email
        private String email;
        @NotBlank
        private String name;
        private String description;
    }

    @Data
    static class MemberPasswordUpdateRequest {
        private String currentPassword;
        private String newPassword;
    }

    @Data
    static class MemberStatusUpdateRequest {
        private MemberStatus memberStatus;
    }

    @Data
    static class MemberResponse {
        private final UUID id;
        private final String email;
        private final String name;
        private final LocalDateTime passwordExpired;
        private final String status;
        private final String description;

        public MemberResponse(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.name = member.getName();
            this.passwordExpired = member.getPasswordExpired();
            this.status = (member.getStatus() == null) ? null : member.getStatus().toString();
            this.description = member.getDescription();
        }
    }
}
