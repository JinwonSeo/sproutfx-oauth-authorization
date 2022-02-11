package kr.sproutfx.oauth.authorization.api.authorize.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectWriter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import kr.sproutfx.oauth.authorization.api.authorize.dto.AuthenticationWithSignedMember;
import kr.sproutfx.oauth.authorization.api.authorize.dto.AuthorizedClient;
import kr.sproutfx.oauth.authorization.api.authorize.dto.ClientKeyWithAuthentication;
import kr.sproutfx.oauth.authorization.api.authorize.dto.ClientKeyWithAuthorizedClient;
import kr.sproutfx.oauth.authorization.api.authorize.dto.ClientKeyWithRefreshToken;
import kr.sproutfx.oauth.authorization.api.authorize.dto.SignedMember;
import kr.sproutfx.oauth.authorization.api.authorize.service.AuthorizeService;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SpringBootTest
public class AuthorizeControllerTest {

    @MockBean
    AuthorizeService authorizeService;
    @MockBean
    ClientService clientService;
    @Autowired
    ObjectWriter objectWriter;
    @Autowired
    MockMvc mockMvc;
    
    @Test
    void testGetAuthorize() throws Exception {
        // given
        final String clientCode = "clientCode";
        given(this.authorizeService.getAuthorize(any())).willReturn(
            ClientKeyWithAuthorizedClient.builder()
                .clientKey("clientKey")
                .authorizedClient(AuthorizedClient.builder()
                    .id(UUID.randomUUID())
                    .code(clientCode)
                    .name("name")
                    .status(ClientStatus.ACTIVE)
                    .description("description")
                    .build()
                ).build()
        );
        // when
        ResultActions resultActions = this.mockMvc.perform(get("/authorize")
            .param("clientCode", clientCode)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
        );
        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(result -> {
                log.info(result.getRequest().getRequestURL().toString());
                log.info(result.getRequest().getParameter("clientCode"));
                log.info(result.getResponse().getContentAsString());
            });
    }

    @Test
    void testPostRefresh() throws Exception {
        // given
        String requestJson = 
            this.objectWriter.writeValueAsString(ClientKeyWithAuthentication.builder()
                .clientKey("clientKey")
                .email("email")
                .password("password")
                .build());

        given(this.authorizeService.postToken(any())).willReturn(
            AuthenticationWithSignedMember.builder()
                .tokenType("Bearer")
                .accessToken("accessToken")
                .accessTokenExpiresIn(1L)
                .refreshToken("refreshToken")
                .refreshTokenExpiresIn(1L)
                .signedMember(SignedMember.builder()
                    .email("email")
                    .name("name")
                    .status(MemberStatus.ACTIVE)
                    .description("description")
                    .build())
                .build()
        );
        // when
        ResultActions resultActions = this.mockMvc.perform(post("/token")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(requestJson)
        );
        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(result -> {
                log.info(result.getRequest().getRequestURL().toString());
                log.info(result.getRequest().getContentAsString());
                log.info(result.getResponse().getContentAsString());
            });
    }

    @Test
    void testPostToken() throws Exception {
        // given
        String requestJson = 
            this.objectWriter.writeValueAsString(ClientKeyWithRefreshToken.builder()
                .clientKey("clientKey")
                .refreshToken("refreshToken"));

        given(this.authorizeService.postRefresh(any())).willReturn(
            AuthenticationWithSignedMember.builder()
                .tokenType("Bearer")
                .accessToken("newAccessToken")
                .accessTokenExpiresIn(1L)
                .refreshToken("newRefreshToken")
                .refreshTokenExpiresIn(1L)
                .signedMember(SignedMember.builder()
                    .email("email")
                    .name("name")
                    .status(MemberStatus.ACTIVE)
                    .description("description")
                    .build())
                .build()
        );
        // when
        ResultActions resultActions = this.mockMvc.perform(post("/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(requestJson)
        );
        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(result -> {
                log.info(result.getRequest().getRequestURL().toString());
                log.info(result.getRequest().getContentAsString());
                log.info(result.getResponse().getContentAsString());
            });
    }
}
