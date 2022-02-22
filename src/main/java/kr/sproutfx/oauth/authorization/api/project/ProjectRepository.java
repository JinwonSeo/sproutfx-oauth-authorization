package kr.sproutfx.oauth.authorization.api.project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
    @Query("SELECT distinct p FROM Project p left join fetch p.clients c")
    public List<Project> findAllWithClients();

    @Query("SELECT distinct p FROM Project p left join fetch p.clients c WHERE p.id = ?1")
    public Optional<Project> findByIdWithClients(UUID id);
}
