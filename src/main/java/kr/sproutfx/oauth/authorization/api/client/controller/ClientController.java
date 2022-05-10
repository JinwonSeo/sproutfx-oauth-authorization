package kr.sproutfx.oauth.authorization.api.client.controller;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;
import kr.sproutfx.oauth.authorization.common.base.BaseController;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/clients")
@Validated
public class ClientController extends BaseController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<StructuredBody<List<ClientResponse>>> findAll() {
        return ResponseEntity.ok().body(StructuredBody.content(
            this.clientService.findAll().stream().map(ClientResponse::new).collect(toList())));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<StructuredBody<ClientResponse>> findById(@PathVariable("id") UUID id) {
        Client selectedClient = this.clientService.findById(id);

        return ResponseEntity.ok().body(StructuredBody.content(new ClientResponse(selectedClient)));
    }

    @PostMapping
    public ResponseEntity<StructuredBody<ClientResponse>> create(@RequestBody @Validated ClientCreateRequest clientCreateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        UUID id = this.clientService.create(clientCreateRequest.getName(), clientCreateRequest.getDescription());

        Client updatedClient = this.clientService.findById(id);

        return ResponseEntity.created(URI.create(String.format("/clients/%s", id))).body(StructuredBody.content(new ClientResponse(updatedClient)));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<StructuredBody<ClientResponse>> update(@PathVariable UUID id, @RequestBody @Validated ClientUpdateRequest clientUpdateRequest, Errors errors) {

        if (errors.hasErrors()) throw new InvalidArgumentException();

        String name = clientUpdateRequest.getName();
        Long accessTokenValidityInSeconds = clientUpdateRequest.getAccessTokenValidityInSeconds();

        String description = clientUpdateRequest.getDescription();

        this.clientService.update(id, name, accessTokenValidityInSeconds, description);

        Client updatedClient = this.clientService.findById(id);

        return ResponseEntity.ok().body(StructuredBody.content(new ClientResponse(updatedClient)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StructuredBody<ClientResponse>> updateStatus(@PathVariable UUID id, @RequestBody ClientStatusUpdateRequest clientStatusUpdateRequest) {

        this.clientService.updateStatus(id, clientStatusUpdateRequest.getClientStatus());

        Client updatedClient = this.clientService.findById(id);

        return ResponseEntity.ok().body(StructuredBody.content(new ClientResponse(updatedClient)));
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
        @Min(3600)
        @Max(7200)
        private Long accessTokenValidityInSeconds;
        private String description;
    }

    @Data
    static class ClientStatusUpdateRequest {
        private ClientStatus clientStatus;
    }
}
