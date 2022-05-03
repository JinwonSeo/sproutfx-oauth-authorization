package kr.sproutfx.oauth.authorization.api.member.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import kr.sproutfx.oauth.authorization.api.member.controller.MemberController.MemberCreateRequest;
import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;

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

    Member[] mockupMembers = { mockupMember1, mockupMember2 };

    Member mockupMember = Member.builder()
            .id(UUID.fromString("000399f9-4b3a-4007-876a-03e9da7669e5"))
            .email("created-member@test.com")
            .name("Created member")
            .password("createdmemberpasswd")
            .passwordExpired(LocalDateTime.now().plusMonths(1))
            .status(MemberStatus.ACTIVE)
            .description(null)
            .build();

    @Test
    void testCreate() throws JsonProcessingException, Exception {
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
        ResultActions resultActions = this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charset.forName("UTF-8"))
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaTypes.HAL_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("succeeded").value(true))
                .andExpect(jsonPath("result.name").value(mockupMember.getName()));
    }

    @Test
    void testDelete() throws Exception {
        // when
        ResultActions perform = this.mockMvc.perform(delete(String.format("/members/%s", mockupMember.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charset.forName("UTF-8"))
                .accept(MediaTypes.HAL_JSON));
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
        ResultActions resultActions = this.mockMvc.perform(get("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charset.forName("UTF-8"))
                .accept(MediaTypes.HAL_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("succeeded").value(true))
                .andExpect(jsonPath("result", Matchers.hasSize(2)));
    }

    @Test
    void testFindById() throws Exception {
        // given
        given(this.memberService.findById(mockupMember.getId()))
                .willReturn(mockupMember);

        // when
        ResultActions resultActions = this.mockMvc.perform(get(String.format("/members/%s", mockupMember.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Charset.forName("UTF-8"))
                .accept(MediaTypes.HAL_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("succeeded").value(true))
                .andExpect(jsonPath("result.name").value(mockupMember.getName()));
    }

    @Test
    void testUpdate() {

    }

    @Test
    void testUpdatePassword() {

    }

    @Test
    void testUpdateStatus() {

    }
}
