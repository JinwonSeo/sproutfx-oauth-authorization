package kr.sproutfx.oauth.authorization.api.client.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import kr.sproutfx.oauth.authorization.api.client.controller.ClientController.ClientCreateRequest;
import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SpringBootTest
public class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Client MOCK_RECODE_1 = Client.builder()
        .id(UUID.fromString("00174dec-5665-4760-9255-6feb51a2b980"))
        .code("d36c646af3104e4587e6a1dfd7a398d0")
        .secret("ejRTUHNKTldTcUJiVWh0c2dXRVNlMGhJMDdxWEJLM0M=")
        .name("Test client #1")
        .accessTokenSecret("agvHf3DN8zrjM9eyM0lYiV6o3obyacgJCOSm7x2gCgua90whlpSY2AbfcU1pjNEzDzxWPqnqHZVTJdlw5AFku2T9oWlvJEgI")
        .accessTokenValidityInSeconds(7200L)
        .status(ClientStatus.ACTIVE)
        .description(null)
        .build();

    Client MOCK_RECODE_2 = Client.builder()
        .id(UUID.fromString("001cae5a-fd6b-43a5-ba74-07ce7bba256b"))
        .code("68c2e7fbf4044c6081ab68b742d65a3d")
        .secret("WHlzaE9oYWtoOElGR0k2SjE5Wnc2Vm5Ybkd2NVF6UVY=")
        .name("Test client #2")
        .accessTokenSecret("5iAJvOfyjrMHkYpnAZRUCnnK4ckKBDirfhKGcZAJOLCOvgLMLXHSBB841Lfot9jOXwl5h291GdBQPrE3cL5uxKQJ5FXCSX4D")
        .accessTokenValidityInSeconds(7200L)
        .status(ClientStatus.ACTIVE)
        .description(null)
        .build();

    @Test
    void testFindAll() throws Exception {
                
        // given
        List<Client> clients = new ArrayList<Client>();

        clients.add(MOCK_RECODE_1);
        clients.add(MOCK_RECODE_2); 

        given(this.clientService.findAll()).willReturn(clients);

        // when
        ResultActions resultAction = this.mockMvc.perform(get("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(Charset.forName("UTF-8"))
            .accept(MediaTypes.HAL_JSON));

        // then
        resultAction
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result", Matchers.hasSize(2)))
            .andDo(print());
    }

    @Test
    void testCreate() throws Exception {
        
        // given
        ClientCreateRequest clientCreateRequest = new ClientCreateRequest();
        clientCreateRequest.setName(MOCK_RECODE_1.getName());
        clientCreateRequest.setDescription(MOCK_RECODE_1.getDescription());

        given(this.clientService.create(clientCreateRequest.getName(), clientCreateRequest.getDescription()))
            .willReturn(MOCK_RECODE_1.getId());

        given(this.clientService.findById(MOCK_RECODE_1.getId()))
            .willReturn(MOCK_RECODE_1);

        // when
        ResultActions resultActions = this.mockMvc.perform(post("/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(Charset.forName("UTF-8"))
            .content(objectMapper.writeValueAsString(clientCreateRequest))
            .accept(MediaTypes.HAL_JSON));

        // then
        resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result.code").exists())
            .andDo(print());
    }

    @Test
    void testDelete() {

    }

    

    @Test
    void testFindById() {
        
    }

    @Test
    void testUpdate() {

    }

    @Test
    void testUpdateStatus() {

    }
}
