package kr.sproutfx.oauth.authorization.api.client.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import kr.sproutfx.oauth.authorization.api.client.exception.ClientNotFoundException;
import kr.sproutfx.oauth.authorization.api.client.model.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.model.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.repository.ClientRepository;
import kr.sproutfx.oauth.authorization.api.client.repository.specification.ClientSpecification;

@Service
@Transactional(readOnly = true)
public class ClientService {
    private final ClientRepository clientRepository;
    
    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return this.clientRepository.findAll();
    }

    public Client findById(UUID id) {
        return this.clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
    }

    public Client findByCode(String code) {
        return this.clientRepository.findOne(ClientSpecification.equalCode(code)).orElseThrow(ClientNotFoundException::new);
    }

    public Client findBySecret(String secret) {
        return this.clientRepository.findOne(ClientSpecification.equalSecret(secret)).orElseThrow(ClientNotFoundException::new);
    }

    @Transactional
    public Client create(String name, String description) {
        return this.clientRepository.save(Client.builder()
            .code(UUID.randomUUID().toString().replace("-", StringUtils.EMPTY))
            .name(name)
            .secret(Base64Utils.encodeToUrlSafeString(RandomStringUtils.randomAlphanumeric(32).getBytes()))
            .status(ClientStatus.PENDING_APPROVAL)
            .accessTokenSecret(RandomStringUtils.randomAlphanumeric(96))
            .accessTokenValidityInSeconds(7200)
            .refreshTokenSecret(RandomStringUtils.randomAlphanumeric(96))
            .refreshTokenValidityInSeconds(43200)
            .description(description)
            .build());
    }

    @Transactional
    public Client update(UUID id, String name, Long accessTokenValidityInSeconds, Long refreshTokenValidityInSeconds, String description) {
        Client persistenceClient = this.clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        
        persistenceClient.setName(name);
        persistenceClient.setAccessTokenValidityInSeconds(accessTokenValidityInSeconds);
        persistenceClient.setRefreshTokenValidityInSeconds(refreshTokenValidityInSeconds);
        persistenceClient.setDescription(description);

        return persistenceClient;
    }

    @Transactional
    public Client deleteById(UUID id) {
        Client persistenceClient = this.clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        
        this.clientRepository.delete(persistenceClient);

        return persistenceClient;
    }
}
