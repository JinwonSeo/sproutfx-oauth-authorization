package kr.sproutfx.oauth.authorization.api.member.controller;

import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;
import kr.sproutfx.oauth.authorization.common.base.BaseController;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController extends BaseController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    private Links links(Member member) {
        Link[] additionalLinks = {
            linkTo(methodOn(this.getClass()).updatePassword(member.getEmail(), new MemberPasswordUpdateRequest(), null)).withRel("update-password"),
            linkTo(methodOn(this.getClass()).updateStatus(member.getId(), new MemberStatusUpdateRequest(), null)).withRel("update-status"),
        };

        return Links.of(getSingleItemLinks(this.getClass(), member.getId(), additionalLinks));
    }

    @GetMapping
    public ResponseEntity<StructuredBody<List<ObjectEntityModel<MemberResponse>>>>
    findAll() {

        return ResponseEntity.ok(StructuredBody.contents(
            this.memberService.findAll().stream().map(member ->
                new ObjectEntityModel<>(new MemberResponse(member), links(member))).collect(Collectors.toList())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StructuredBody<ObjectEntityModel<MemberResponse>>> findById(@PathVariable UUID id) {

        Member selectedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.contents(
            new ObjectEntityModel<>(new MemberResponse(selectedMember), links(selectedMember))));
    }

    @PostMapping
    public ResponseEntity<StructuredBody<ObjectEntityModel<MemberResponse>>>
    create(@RequestBody @Validated MemberCreateRequest memberCreateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        UUID id = this.memberService.create(
            memberCreateRequest.getEmail(),
            memberCreateRequest.getName(),
            memberCreateRequest.getPassword(),
            memberCreateRequest.getDescription());

        Member createdMember = this.memberService.findById(id);

        return ResponseEntity.created(linkTo(methodOn(this.getClass()).findById(createdMember.getId())).toUri()).body(StructuredBody.contents(
            new ObjectEntityModel<>(new MemberResponse(createdMember), links(createdMember))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StructuredBody<ObjectEntityModel<MemberResponse>>>
    update(@PathVariable UUID id, @RequestBody @Validated MemberUpdateRequest memberUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        String email = memberUpdateRequest.getEmail();
        String name = memberUpdateRequest.getName();
        String description = memberUpdateRequest.getDescription();

        this.memberService.update(id, email, name, description);

        Member updatedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.contents(
            new ObjectEntityModel<>(new MemberResponse(updatedMember), links(updatedMember))));
    }

    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<StructuredBody<ObjectEntityModel<MemberResponse>>>
    updateStatus(@PathVariable UUID id, @RequestBody @Validated MemberStatusUpdateRequest memberStatusUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        MemberStatus memberStatus = memberStatusUpdateRequest.getMemberStatus();

        this.memberService.updateStatus(id, memberStatus);

        Member updatedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.contents(
            new ObjectEntityModel<>(new MemberResponse(updatedMember), links(updatedMember))));
    }

    @PatchMapping(value = "/{email}/password")
    public ResponseEntity<StructuredBody<ObjectEntityModel<MemberResponse>>>
    updatePassword(@PathVariable String email, @RequestBody @Validated MemberPasswordUpdateRequest memberPasswordUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        String currentPassword = memberPasswordUpdateRequest.getCurrentPassword();
        String newPassword = memberPasswordUpdateRequest.getNewPassword();

        UUID id = this.memberService.updatePassword(email, currentPassword, newPassword);

        Member updatedMember = this.memberService.findById(id);

        return ResponseEntity.ok(StructuredBody.contents(
            new ObjectEntityModel<>(new MemberResponse(updatedMember), links(updatedMember))));
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
        private String email;
        private String name;
        private LocalDateTime passwordExpired;
        private String status;
        private String description;

        public MemberResponse(Member member) {
            this.email = member.getEmail();
            this.name = member.getName();
            this.passwordExpired = member.getPasswordExpired();
            this.status = (member.getStatus() == null) ? null : member.getStatus().toString();
            this.description = member.getDescription();
        }
    }
}
