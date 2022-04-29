package kr.sproutfx.oauth.authorization.api.client.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import kr.sproutfx.oauth.authorization.common.base.BaseController;

import lombok.Data;
import lombok.Getter;

@RestController
@RequestMapping("/clients")
@Validated
public class ClientController extends BaseController {

    private final ClientService clientService;
        
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    private Links links(Client client) {
        Link[] additionalLinks = {
            linkTo(methodOn(this.getClass()).updateStatus(client.getId(), new ClientStatusUpdateRequest())).withRel("update-status"),
        };

        return Links.of(getSingleItemLinks(this.getClass(), client.getId(), additionalLinks));
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<ObjectEntityModel<ClientResponse>>>> 
            findAll() {

        return ResponseEntity.ok(
            new ResponseBody<>(this.clientService.findAll().stream().map(client -> 
                new ObjectEntityModel<>(
                    new ClientResponse(client), links(client))).collect(Collectors.toList())));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseBody<ObjectEntityModel<ClientResponse>>>
            findById(@PathVariable("id") UUID id) {

        Client client = this.clientService.findById(id);
        
        return ResponseEntity.ok(
            new ResponseBody<>(
                new ObjectEntityModel<>(
                    new ClientResponse(client), links(client))));
    }

    @PostMapping
    public ResponseEntity<ResponseBody<ObjectEntityModel<ClientResponse>>>
            create(@RequestBody @Validated ClientCreateRequest clientCreateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();
        
        UUID id = this.clientService.create(clientCreateRequest.getName(), clientCreateRequest.getDescription());

        Client updatedClient = this.clientService.findById(id);

        return ResponseEntity.created(linkTo(this.getClass()).slash(id).toUri()).body(
            new ResponseBody<>(
                new ObjectEntityModel<>(
                    new ClientResponse(updatedClient), links(updatedClient))));
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ResponseBody<ObjectEntityModel<ClientResponse>>> 
            update(@PathVariable UUID id, @RequestBody @Validated ClientUpdateRequest clientUpdateRequest, Errors errors) {
                
        if (errors.hasErrors()) throw new InvalidArgumentException();
        
        String name = clientUpdateRequest.getName();
        Long accessTokenValidityInSeconds = clientUpdateRequest.getAccessTokenValidityInSeconds();

        String description = clientUpdateRequest.getDescription();

        this.clientService.update(id, name, accessTokenValidityInSeconds, description);

        Client updatedClient = this.clientService.findById(id);

        return ResponseEntity.ok().body(
            new ResponseBody<>(
                new ObjectEntityModel<>(
                    new ClientResponse(updatedClient), links(updatedClient))));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseBody<ObjectEntityModel<ClientResponse>>>
            updateStatus(@PathVariable UUID id, @RequestBody ClientStatusUpdateRequest clientStatusUpdateRequest) {

        this.clientService.updateStatus(id, clientStatusUpdateRequest.getClientStatus());

        Client updatedClient = this.clientService.findById(id);

        return ResponseEntity.ok().body(
            new ResponseBody<>(
                new ObjectEntityModel<>(
                    new ClientResponse(updatedClient), links(updatedClient))));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {

        this.clientService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Getter
    static class ClientResponse {
        private final String code;
        private final String name;
        private final String status;
        private final String description;

        public ClientResponse(Client client) {
            this.code = client.getCode();
            this.name = client.getName();
            this.status = (client.getStatus() == null) ? null : client.getStatus().toString();
            this.description = client.getDescription();
        }
    }

    @Data
    static class ClientCreateRequest {
        @NotBlank
        private String name;
        private String description;
    }

    @Data
    static class ClientUpdateRequest {
        @NotBlank
        private String name;
        @Min(3600) @Max(7200)
        private Long accessTokenValidityInSeconds;
        private String description;
    }

    @Data
    static class ClientStatusUpdateRequest {
        private ClientStatus clientStatus;
    }
}
