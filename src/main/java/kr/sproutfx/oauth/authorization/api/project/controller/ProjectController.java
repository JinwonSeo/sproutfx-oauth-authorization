package kr.sproutfx.oauth.authorization.api.project.controller;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import kr.sproutfx.oauth.authorization.api.project.enumeration.ProjectStatus;
import kr.sproutfx.oauth.authorization.api.project.service.ProjectService;
import kr.sproutfx.oauth.authorization.common.base.BaseController;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import lombok.Data;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("projects")
public class ProjectController extends BaseController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    private Links links(Project project) {
        return Links.of(getSingleItemLinks(this.getClass(), project.getId()));
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<ObjectEntityModel<ProjectResponse>>>> 
            findAll() {

        return ResponseEntity.ok().body(
            new ResponseBody<>(this.projectService.findAll().stream().map(project -> 
                new ObjectEntityModel<>(new ProjectResponse(project), links(project))).collect(toList())));
    }

    @GetMapping(value = "/clients")
    public ResponseEntity<ResponseBody<List<ObjectEntityModel<ProjectWithClientsResponse>>>> 
            findAllWithClients() {

        return ResponseEntity.ok().body(
            new ResponseBody<>(this.projectService.findAllWithClients().stream().map(project -> 
                new ObjectEntityModel<>(new ProjectWithClientsResponse(project), links(project))).collect(toList())));
    }    

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBody<ObjectEntityModel<ProjectResponse>>> 
            findById(@PathVariable UUID id) {

        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.ok().body(
            new ResponseBody<>(
                new ObjectEntityModel<>(new ProjectResponse(selectedProject), links(selectedProject))));
    }

    @PostMapping
    public ResponseEntity<ResponseBody<ObjectEntityModel<ProjectResponse>>> 
            create(@RequestBody @Validated ProjectCreateRequest projectCreateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();
        
        UUID id = this.projectService.create(projectCreateRequest.getName(), projectCreateRequest.getDescription());
        
        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.created(linkTo(this.getClass()).slash(id).toUri()).body(
            new ResponseBody<>(
                new ObjectEntityModel<>(new ProjectResponse(selectedProject), links(selectedProject))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody<ObjectEntityModel<ProjectResponse>>> 
            update(@PathVariable UUID id, @RequestBody @Validated ProjectUpdateRequest projectUpdateRequest, Errors errors) {
                
        if (errors.hasErrors()) throw new InvalidArgumentException();

        this.projectService.update(id, projectUpdateRequest.getName(), projectUpdateRequest.getDescription());

        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.ok().body(
            new ResponseBody<>(
                new ObjectEntityModel<>(new ProjectResponse(selectedProject), links(selectedProject))));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseBody<ObjectEntityModel<ProjectResponse>>> 
            updateStatus(@PathVariable UUID id, @RequestBody @Validated ProjectStatusUpdateRequest projectStatusUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        this.projectService.updateStatus(id, projectStatusUpdateRequest.getProjectStatus());

        Project selectedProject = this.projectService.findById(id);

        return ResponseEntity.ok().body(
            new ResponseBody<>(
                new ObjectEntityModel<>(new ProjectResponse(selectedProject), links(selectedProject))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {
        
        this.projectService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Data
    static class ProjectWithClientsResponse {
        private UUID id;
        private String name;
        private String status;
        private String description;
        private List<ClientResponse> clients;

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
        private String name;
        private String status;
        private String description;

        public ProjectResponse(Project project) {
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
        private String code;
        private String name;
        private String status;
        private String description;

        public ClientResponse(Client client) {
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
