package kr.sproutfx.oauth.authorization.api.authorize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.sproutfx.oauth.authorization.api.authorize.dto.ClientKeyWithAuthentication;
import kr.sproutfx.oauth.authorization.api.authorize.dto.ClientKeyWithRefreshToken;
import kr.sproutfx.oauth.authorization.api.authorize.dto.ClientKeyWithAuthorizedClient;
import kr.sproutfx.oauth.authorization.api.authorize.dto.AuthenticationWithSignedMember;
import kr.sproutfx.oauth.authorization.api.authorize.service.AuthorizeService;
import kr.sproutfx.oauth.authorization.common.dto.Response;

@RestController
@RequestMapping("/")
public class AuthorizeController {
    private final AuthorizeService authorizeService;

    @Autowired
    public AuthorizeController(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;
    }

    @GetMapping("/authorize")
    public Response<ClientKeyWithAuthorizedClient> getAuthorize(@RequestParam String clientCode) {
        return new Response<>(this.authorizeService.getAuthorize(clientCode));
    }

    @PostMapping("/token")
    public Response<AuthenticationWithSignedMember> postToken(@RequestBody ClientKeyWithAuthentication clientKeyWithAuthentication) {
        return new Response<>(this.authorizeService.postToken(clientKeyWithAuthentication));
    }

    @PostMapping("/refresh")
    public Response<AuthenticationWithSignedMember> postRefresh(@RequestBody ClientKeyWithRefreshToken clientKeyWithRefreshToken) {
        return new Response<>(this.authorizeService.postRefresh(clientKeyWithRefreshToken));
    }
}
