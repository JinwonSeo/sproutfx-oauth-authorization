package kr.sproutfx.oauth.authorization.api.project.controller;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import kr.sproutfx.oauth.authorization.api.project.enumeration.ProjectStatus;
import kr.sproutfx.oauth.authorization.api.project.service.ProjectService;
import kr.sproutfx.oauth.authorization.common.base.BaseController;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "projects")
public class ProjectController extends BaseController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<StructuredBody<List<ProjectResponse>>> findAll() {

        return ResponseEntity.ok().body(StructuredBody.content(
            this.projectService.findAll().stream().map(ProjectResponse::new).collect(toList())));
    }

    @GetMapping(value = "/clients")
    public ResponseEntity<StructuredBody<List<ProjectWithClientsResponse>>> findAllWithClients() {

        return ResponseEntity.ok().body(StructuredBody.content(
            this.projectService.findAllWithClients().stream().map(ProjectWithClientsResponse::new).collect(toList())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StructuredBody<ProjectResponse>> findById(@PathVariable UUID id) {

        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.ok().body(StructuredBody.content(
            new ProjectResponse(selectedProject)));
    }

    @PostMapping
    public ResponseEntity<StructuredBody<ProjectResponse>> create(@RequestBody @Validated ProjectCreateRequest projectCreateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        UUID id = this.projectService.create(projectCreateRequest.getName(), projectCreateRequest.getDescription());

        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.created(URI.create(String.format("/projects/%s", id))).body(StructuredBody.content(
            new ProjectResponse(selectedProject)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StructuredBody<ProjectResponse>> update(@PathVariable UUID id, @RequestBody @Validated ProjectUpdateRequest projectUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        this.projectService.update(id, projectUpdateRequest.getName(), projectUpdateRequest.getDescription());

        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.ok().body(StructuredBody.content(
            new ProjectResponse(selectedProject)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StructuredBody<ProjectResponse>> updateStatus(@PathVariable UUID id, @RequestBody @Validated ProjectStatusUpdateRequest projectStatusUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        this.projectService.updateStatus(id, projectStatusUpdateRequest.getProjectStatus());

        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.ok().body(StructuredBody.content(
            new ProjectResponse(selectedProject)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {

        this.projectService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Data
    static class ProjectWithClientsResponse {
        private final UUID id;
        private final String name;
        private final String status;
        private final String description;
        private final List<ClientResponse> clients;

        public ProjectWithClientsResponse(Project project) {
            this.id = project.getId();
            this.name = project.getName();
            this.status = project.getStatus().toString();
            this.description = project.getDescription();
            this.clients = project.getClients().stream().map(ClientResponse::new).collect(toList());
        }
    }

    @Data
    static class ProjectResponse {
        private final UUID id;
        private final String name;
        private final String status;
        private final String description;

        public ProjectResponse(Project project) {
            this.id = project.getId();
            this.name = project.getName();
            this.status = project.getStatus().toString();
            this.description = project.getDescription();
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
        private final UUID id;
        private final String code;
        private final String name;
        private final String status;
        private final String description;

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

    @Data
    static class ProjectStatusUpdateRequest {
        private ProjectStatus projectStatus;
    }
}
