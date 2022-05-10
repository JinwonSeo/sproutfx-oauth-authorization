package kr.sproutfx.oauth.authorization.api.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import kr.sproutfx.oauth.authorization.api.member.controller.MemberController.MemberCreateRequest;
import kr.sproutfx.oauth.authorization.api.member.controller.MemberController.MemberUpdateRequest;
import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

import static kr.sproutfx.oauth.authorization.api.member.controller.MemberController.MemberStatusUpdateRequest;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Mockup recode
    Member mockupMember1 = Member.builder()
        .id(UUID.fromString("0001e564-2a07-4fd3-b69a-188678f36f3f"))
        .email("test-member1@test.com")
        .name("Test Member #1")
        .password("testmember1passwd")
        .passwordExpired(LocalDateTime.now().plusMonths(1))
        .status(MemberStatus.ACTIVE)
        .description(null)
        .build();

    Member mockupMember2 = Member.builder()
        .id(UUID.fromString("0001e830-df36-44f4-af07-f164e185548f"))
        .email("test-member2@test.com")
        .name("Test Member #2")
        .password("testmember2passwd")
        .passwordExpired(LocalDateTime.now().plusMonths(1))
        .status(MemberStatus.ACTIVE)
        .description(null)
        .build();

    Member[] mockupMembers = {mockupMember1, mockupMember2};

    Member mockupMember = Member.builder()
        .id(UUID.fromString("000399f9-4b3a-4007-876a-03e9da7669e5"))
        .email("created-member@test.com")
        .name("Created member")
        .password("createdmemberpasswd")
        .passwordExpired(LocalDateTime.now().plusMonths(1))
        .status(MemberStatus.PENDING_APPROVAL)
        .description(null)
        .build();

    @Test
    void testCreate() throws Exception {
        // given
        String unEncodedPassword = "createdmemberpasswd";

        MemberCreateRequest request = new MemberCreateRequest();
        request.setName(mockupMember.getName());
        request.setEmail(mockupMember.getEmail());
        request.setPassword(unEncodedPassword);
        request.setDescription(null);

        given(this.memberService.create(request.getEmail(), request.getName(), request.getPassword(), request.getDescription()))
            .willReturn(mockupMember.getId());

        given(this.memberService.findById(mockupMember.getId()))
            .willReturn(mockupMember);

        // when
        ResultActions perform = this.mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(objectMapper.writeValueAsString(request)));

        // then
        perform.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("content.name").value(mockupMember.getName()));
    }

    @Test
    void testDelete() throws Exception {
        // when
        ResultActions perform = this.mockMvc.perform(delete(String.format("/members/%s", mockupMember.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));
        // then
        perform.andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void testFindAll() throws Exception {
        // given
        given(this.memberService.findAll())
            .willReturn(Lists.newArrayList(mockupMembers));

        // when
        ResultActions perform = this.mockMvc.perform(get("/members")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        perform.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("content", Matchers.hasSize(2)));
    }

    @Test
    void testFindById() throws Exception {
        // given
        given(this.memberService.findById(mockupMember.getId()))
            .willReturn(mockupMember);

        // when
        ResultActions perform = this.mockMvc.perform(get(String.format("/members/%s", mockupMember.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        perform.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("content.name").value(mockupMember.getName()));
    }

    @Test
    void testUpdate() throws Exception {
        // given
        MemberUpdateRequest request = new MemberUpdateRequest();
        request.setEmail("updated-email@test.com");
        request.setName("Updated name");
        request.setDescription("Updated description");

        mockupMember.setEmail(request.getEmail());
        mockupMember.setName(request.getName());
        mockupMember.setDescription(request.getDescription());

        given(this.memberService.findById(mockupMember.getId()))
            .willReturn(mockupMember);

        // when
        ResultActions perform = this.mockMvc.perform(put(String.format("/members/%s", mockupMember.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(objectMapper.writeValueAsString(request)));

        // then
        perform.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("content.name").value(mockupMember.getName()));
    }

    @Test
    void testUpdateStatus() throws Exception {
        // given
        MemberStatusUpdateRequest request = new MemberStatusUpdateRequest();
        request.setMemberStatus(MemberStatus.ACTIVE);

        given(this.memberService.findById(mockupMember.getId()))
            .willReturn(mockupMember);

        // when
        ResultActions perform = this.mockMvc.perform(patch(String.format("/members/%s/status", mockupMember.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(objectMapper.writeValueAsString(request)));

        // then
        perform.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("content.name").value(mockupMember.getName()));
    }
}
