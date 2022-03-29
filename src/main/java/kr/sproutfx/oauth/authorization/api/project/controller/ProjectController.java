package kr.sproutfx.oauth.authorization.api.project.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import kr.sproutfx.oauth.authorization.api.project.service.ProjectService;
import kr.sproutfx.oauth.authorization.common.base.BaseController;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;

import lombok.Data;

@RestController
@RequestMapping("projects")
public class ProjectController extends BaseController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public Response<List<ProjectResponse>> findAll() {
        return new Response<>(this.projectService.findAll().stream().map(ProjectResponse::new).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public Response<ProjectResponse> findById(@PathVariable UUID id) {
        return new Response<>(new ProjectResponse(this.projectService.findById(id)));
    }

    @PostMapping
    public Response<ProjectResponse> create(@RequestBody @Validated ProjectCreateRequest projectCreateRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();
        
        UUID id = this.projectService.create(projectCreateRequest.getName(), projectCreateRequest.getDescription());
        
        return new Response<>(new ProjectResponse(this.projectService.findById(id)));
    }

    @PutMapping("/{id}")
    public Response<ProjectResponse> update(@PathVariable UUID id, @RequestBody @Validated ProjectUpdateRequest projectUpdateRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();

        this.projectService.update(id, projectUpdateRequest.getName(), projectUpdateRequest.getDescription());

        return new Response<>(new ProjectResponse(this.projectService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public Response<ProjectDeleteResponse> delete(@PathVariable UUID id) {
        
        this.projectService.delete(id);

        return new Response<>(new ProjectDeleteResponse(id));
    }


    @Data
    static class ProjectResponse {
        private UUID id;
        private String name;
        private String status;
        private String description;
        private List<ClientResponse> clients;

        public ProjectResponse(Project project) {
            this.id = project.getId();
            this.name = project.getName();
            this.status = project.getStatus().toString();
            this.description = project.getDescription();
            this.clients = project.getClients().stream().map(ClientResponse::new).collect(Collectors.toList());
        }
    }

    @Data
    static class ProjectDeleteResponse {
        private UUID deletedProjectId;

        public ProjectDeleteResponse(UUID deletedProjectId) {
            this.deletedProjectId = deletedProjectId;
        }
    }

    @Data
    static class ClientResponse {
        private UUID id;
        private String code;
        private String name;
        private String status;
        private String description;

        public ClientResponse(Client client) {
            this.id = client.getId();
            this.code = client.getCode();
            this.name = client.getName();
            this.status = client.getStatus().toString();
            this.description = client.getDescription();
        }
    }

    @Data
    static class ProjectCreateRequest {
        @NotBlank
        private String name;
        private String description;
    }

    @Data
    static class ProjectUpdateRequest {
        @NotBlank
        private String name;
        private String description;
    }
}
