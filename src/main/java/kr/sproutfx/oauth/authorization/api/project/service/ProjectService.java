package kr.sproutfx.oauth.authorization.api.project.service;

import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import kr.sproutfx.oauth.authorization.api.project.exception.ProjectNotFoundException;
import kr.sproutfx.oauth.authorization.api.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project findById(UUID id) {
        return this.projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);
    }

    public Project findByIdWithClients(UUID id) {
        return this.projectRepository.findByIdWithClients(id).orElseThrow(ProjectNotFoundException::new);
    }
}
