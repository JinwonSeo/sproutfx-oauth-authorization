package kr.sproutfx.oauth.authorization.api.client.repository.specification;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {
    private ClientSpecification() {
        throw new IllegalStateException();
    }

    public static Specification<Client> equalCode(String code) {
        return (Specification<Client>) ((root, query, builder) -> builder.equal(root.get("code"), code));
    }

    public static Specification<Client> equalSecret(String secret) {
        return (Specification<Client>) ((root, query, builder) -> builder.equal(root.get("secret"), secret));
    }
}
