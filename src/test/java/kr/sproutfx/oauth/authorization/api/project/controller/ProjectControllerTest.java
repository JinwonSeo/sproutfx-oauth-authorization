package kr.sproutfx.oauth.authorization.api.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.project.controller.ProjectController.ProjectCreateRequest;
import kr.sproutfx.oauth.authorization.api.project.controller.ProjectController.ProjectStatusUpdateRequest;
import kr.sproutfx.oauth.authorization.api.project.controller.ProjectController.ProjectUpdateRequest;
import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import kr.sproutfx.oauth.authorization.api.project.enumeration.ProjectStatus;
import kr.sproutfx.oauth.authorization.api.project.service.ProjectService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@SpringBootTest
public class ProjectControllerTest {

    @MockBean
    private ProjectService projectService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mockup recode
    Client mockupClient1 = Client.builder()
        .id(UUID.fromString("00174dec-5665-4760-9255-6feb51a2b980"))
        .code("d36c646af3104e4587e6a1dfd7a398d0")
        .secret("ejRTUHNKTldTcUJiVWh0c2dXRVNlMGhJMDdxWEJLM0M=")
        .name("Test client #1")
        .accessTokenSecret("agvHf3DN8zrjM9eyM0lYiV6o3obyacgJCOSm7x2gCgua90whlpSY2AbfcU1pjNEzDzxWPqnqHZVTJdlw5AFku2T9oWlvJEgI")
        .accessTokenValidityInSeconds(7200L)
        .status(ClientStatus.ACTIVE)
        .description(null)
        .build();

    Client mockupClient2 = Client.builder()
        .id(UUID.fromString("001cae5a-fd6b-43a5-ba74-07ce7bba256b"))
        .code("68c2e7fbf4044c6081ab68b742d65a3d")
        .secret("WHlzaE9oYWtoOElGR0k2SjE5Wnc2Vm5Ybkd2NVF6UVY=")
        .name("Test client #2")
        .accessTokenSecret("5iAJvOfyjrMHkYpnAZRUCnnK4ckKBDirfhKGcZAJOLCOvgLMLXHSBB841Lfot9jOXwl5h291GdBQPrE3cL5uxKQJ5FXCSX4D")
        .accessTokenValidityInSeconds(7200L)
        .status(ClientStatus.ACTIVE)
        .description(null)
        .build();

    Client mockupClient3 = Client.builder()
        .id(UUID.fromString("0021cf78-8821-4ee7-898c-5f4e8e32ab37"))
        .code("7689257a6cce464ebeb580cd3c009298")
        .secret("cVFqYmxMeW42UDNtV3Q1dXl1WFNDUU53UlM5SHg3SlA=")
        .name("Test client #3")
        .accessTokenSecret("lPLimNymzrxTTEGwaKB0mmDIzC2HTuFRPGMr2k4IVHc05AUrxYYRc64KB94xkuqXMHuDkDvN7zDe358uMAWChcOFFL8SQI7u")
        .accessTokenValidityInSeconds(7200L)
        .status(ClientStatus.ACTIVE)
        .description(null)
        .build();

    Client mockupClient4 = Client.builder()
        .id(UUID.fromString("00275660-6545-4d40-9c63-3ace62485496"))
        .code("62012c9a75344076a04d2575e96c4c00")
        .secret("a2lBTE5mVHhIeGJkY25CcHNxUjhBV3pwcFVIWGJuNmE=")
        .name("Test client #4")
        .accessTokenSecret("oG1n9T9jQ23bcIGeMl6ZP1JXqJCsaXnz7LN0TmtbdLpHDrIWNgDioNwzfz7X6vBdNC9rXeWZ1pSM9VjeEDAFccLY0tW3VB4v")
        .accessTokenValidityInSeconds(7200L)
        .status(ClientStatus.ACTIVE)
        .description(null)
        .build();

    Client[] mockupClients1 = {mockupClient1, mockupClient2};
    Client[] mockupClients2 = {mockupClient3, mockupClient4};

    Project mockupProject1 = Project.builder()
        .id(UUID.fromString("000c8538-d62b-41d4-bd0d-f4d4dfb5ad4b"))
        .name("Test project #1")
        .status(ProjectStatus.ACTIVE)
        .description(null)
        .clients(Lists.newArrayList(mockupClients1))
        .build();

    Project mockupProject2 = Project.builder()
        .id(UUID.fromString("0043fee4-8746-4628-8aff-1d6d63903691"))
        .name("Test project #2")
        .status(ProjectStatus.ACTIVE)
        .description(null)
        .clients(Lists.newArrayList(mockupClients2))
        .build();

    Project[] mockupProjects = {mockupProject1, mockupProject2};

    Project mockupProject = Project.builder()
        .id(UUID.fromString("004544c0-766e-4ef6-a1eb-bd0c37100d0c"))
        .name("Created project name")
        .status(ProjectStatus.PENDING_APPROVAL)
        .description(null)
        .build();

    @Test
    void testFindAll() throws Exception {
        // given
        given(this.projectService.findAll())
            .willReturn(Lists.newArrayList(mockupProjects));

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result", Matchers.hasSize(2)))
            .andExpect(jsonPath("result.$[0].client.name").doesNotExist());
    }

    @Test
    void testFindAllWithClients() throws Exception {
        // given
        given(this.projectService.findAllWithClients())
            .willReturn(Lists.newArrayList(mockupProjects));

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/projects/clients")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result", Matchers.hasSize(2)))
            .andExpect(jsonPath("result.$[0].client.name").doesNotExist());
    }

    @Test
    void testCreate() throws Exception {
        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest();
        projectCreateRequest.setName(mockupProject.getName());
        projectCreateRequest.setDescription(mockupProject.getDescription());

        // given
        given(this.projectService.create(projectCreateRequest.getName(), projectCreateRequest.getDescription()))
            .willReturn(mockupProject.getId());

        given(this.projectService.findById(mockupProject.getId()))
            .willReturn(mockupProject);

        // when
        ResultActions resultActions = this.mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(objectMapper.writeValueAsString(projectCreateRequest)));

        // then
        resultActions.andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result.name").value(mockupProject.getName()));
    }

    @Test
    void testFindById() throws Exception {
        // given
        given(this.projectService.findById(mockupProject.getId()))
            .willReturn(mockupProject);

        // when
        ResultActions resultActions = this.mockMvc.perform(get(String.format("/projects/%s", mockupProject.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result.name").value(mockupProject.getName()));
    }

    @Test
    void testUpdate() throws Exception {
        // given
        ProjectUpdateRequest request = new ProjectUpdateRequest();
        request.setName("new Name");
        request.setDescription("new Description");

        mockupProject.setName(request.getName());
        mockupProject.setDescription(request.getDescription());

        given(this.projectService.findById(mockupProject.getId()))
            .willReturn(mockupProject);

        // when
        ResultActions resultActions = this.mockMvc.perform(put(String.format("/projects/%s", mockupProject.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result.name").value(request.getName()))
            .andExpect(jsonPath("result.description").value(request.getDescription()));
    }

    @Test
    void testUpdateStatus() throws Exception {
        // given
        ProjectStatusUpdateRequest request = new ProjectStatusUpdateRequest();
        request.setProjectStatus(ProjectStatus.ACTIVE);

        mockupProject.setStatus(request.getProjectStatus());

        given(this.projectService.findById(mockupProject.getId()))
            .willReturn(mockupProject);

        // when
        ResultActions resultActions = this.mockMvc.perform(patch(String.format("/projects/%s/status", mockupProject.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8)
            .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("succeeded").value(true))
            .andExpect(jsonPath("result.status").value(mockupProject.getStatus().toString()));
    }

    @Test
    void testDelete() throws Exception {
        // when
        ResultActions resultActions = this.mockMvc.perform(delete(String.format("/projects/%s", mockupProject.getId()))
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding(StandardCharsets.UTF_8));

        // then
        resultActions.andDo(print())
            .andExpect(status().isNoContent());
    }
}
