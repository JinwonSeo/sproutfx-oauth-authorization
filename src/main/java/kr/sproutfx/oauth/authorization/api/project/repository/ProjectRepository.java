package kr.sproutfx.oauth.authorization.api.project.repository;

import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
    @Query("SELECT distinct p FROM Project p left join fetch p.clients c")
    List<Project> findAllWithClients();

    @Query("SELECT distinct p FROM Project p left join fetch p.clients c WHERE p.id = ?1")
    Optional<Project> findByIdWithClients(UUID id);
}
