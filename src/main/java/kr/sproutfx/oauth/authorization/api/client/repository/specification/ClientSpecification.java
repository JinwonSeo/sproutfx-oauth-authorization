package kr.sproutfx.oauth.authorization.api.client.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import kr.sproutfx.oauth.authorization.api.client.model.entity.Client;

public class ClientSpecification {
    private ClientSpecification() { throw new IllegalStateException();}

    public static Specification<Client> equalCode(String code) {
        return (Specification<Client>) ((root, query, builder) -> builder.equal(root.get("code"), code));
    }

    public static Specification<Client> equalSecret(String secret) {
        return (Specification<Client>) ((root, query, builder) -> builder.equal(root.get("secret"), secret));
    }
}
