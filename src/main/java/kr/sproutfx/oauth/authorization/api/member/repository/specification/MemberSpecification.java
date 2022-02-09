package kr.sproutfx.oauth.authorization.api.member.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import kr.sproutfx.oauth.authorization.api.member.model.entity.Member;

public class MemberSpecification {
    private MemberSpecification() { 
        throw new IllegalStateException();
    }

    public static Specification<Member> equalEmail(String email) {
        return (Specification<Member>) ((root, query, builder) -> builder.equal(root.get("email"), email));
    }
}
