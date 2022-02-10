package kr.sproutfx.oauth.authorization.api.client.controller;

import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
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

import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import kr.sproutfx.oauth.authorization.common.model.dto.Response;
import kr.sproutfx.oauth.authorization.common.utility.ModelMapperUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/clients")
@Validated
public class ClientController {
    private final ClientService clientService;
    
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public Response<List<ClientResponse>> findAll() {
        return new Response<>(
            this.clientService.findAll()
                .stream()
                .map(source -> ModelMapperUtils.defaultMapper().map(source, ClientResponse.class))
                .collect(Collectors.toList()));
    }

    @GetMapping(value="/{id}")
    public Response<ClientResponse> findById(@PathVariable("id") UUID id) {
        return new Response<>(
            ModelMapperUtils.defaultMapper().map(
                this.clientService.findById(id), ClientResponse.class));
    }

    @PostMapping
    public Response<ClientResponse> create(@RequestBody @Validated ClientCreateRequest clientCreateRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();
        
        UUID id = this.clientService.create(clientCreateRequest.getName(), clientCreateRequest.getDescription());

        return new Response<>(
            ModelMapperUtils.defaultMapper().map(
                this.clientService.findById(id), ClientResponse.class));
    }

    @PutMapping(value="/{id}")
    public Response<ClientResponse> update(@PathVariable UUID id, @RequestBody @Validated ClientUpdateRequest clientUpdateRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();
        
        this.clientService.update(id, 
                clientUpdateRequest.getName(), 
                clientUpdateRequest.getAccessTokenValidityInSeconds(), 
                clientUpdateRequest.getRefreshTokenValidityInSeconds(), 
                clientUpdateRequest.getDescription());

        return new Response<>(
            ModelMapperUtils.defaultMapper().map(
                this.clientService.findById(id), ClientResponse.class));
    }

    @DeleteMapping(value = "/{id}")
    public Response<ClientDeleteResponse> delete(@PathVariable UUID id) {

        this.clientService.deleteById(id);

        return new Response<>(new ClientDeleteResponse(id));
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
        private long accessTokenValidityInSeconds;
        @Min(3600) @Max(86400)
        private long refreshTokenValidityInSeconds;
        private String description;
    }

    @Data
    static class ClientResponse {
        private UUID id;
        private String code;
        private String name;
        private ClientStatus status;
        private String description;
    }

    @AllArgsConstructor
    @Data
    static class ClientDeleteResponse {
        private UUID deletedClientId;
    }
}
