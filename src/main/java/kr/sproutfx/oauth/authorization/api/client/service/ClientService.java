package kr.sproutfx.oauth.authorization.api.client.service;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.exception.ClientNotFoundException;
import kr.sproutfx.oauth.authorization.api.client.repository.ClientRepository;
import kr.sproutfx.oauth.authorization.api.client.repository.specification.ClientSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findByIdWithProject(UUID id) {
        return this.clientRepository.findByIdWithProject(id).orElseThrow(ClientNotFoundException::new);
    }

    public Client findByCodeWithProject(String code) {
        return this.clientRepository.findByCodeWithProject(code).orElseThrow(ClientNotFoundException::new);
    }

    public Client findByCode(String code) {
        return this.clientRepository.findOne(ClientSpecification.equalCode(code)).orElseThrow(ClientNotFoundException::new);
    }

    public Client findBySecret(String secret) {
        return this.clientRepository.findOne(ClientSpecification.equalSecret(secret)).orElseThrow(ClientNotFoundException::new);
    }


}
