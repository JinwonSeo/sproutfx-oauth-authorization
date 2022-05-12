package kr.sproutfx.oauth.authorization.api.client.repository;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    @Query(value = "SELECT distinct c FROM Client c left join fetch c.project p WHERE c.id = ?1")
    Optional<Client> findByIdWithProject(UUID id);

    @Query(value = "SELECT distinct c FROM Client c left join fetch c.project p WHERE c.code = ?1")
    Optional<Client> findByCodeWithProject(String code);
}
