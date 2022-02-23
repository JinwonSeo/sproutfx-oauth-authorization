package kr.sproutfx.oauth.authorization.api.project;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return this.projectRepository.findAllWithClients();
    }

    public Project findById(UUID id) {
        return this.projectRepository.findByIdWithClients(id).orElseThrow(ProjectNotFoundException::new);
    }

    @Transactional
    public UUID create(String name, String description) {
        Project persistenceProject = this.projectRepository.save(Project.builder()
            .name(name)
            .description(description)
            .status(ProjectStatus.PENDING_APPROVAL)
            .build()); 

        return persistenceProject.getId();
    }

    @Transactional
    public void update(UUID id ,String name, String description) {
        Project persistenceProject = this.projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

        persistenceProject.setName(name);
        persistenceProject.setDescription(description);
    }

    @Transactional
    public void delete(UUID id) {
        Project persistenceProject = this.projectRepository.findById(id).orElseThrow(ProjectNotFoundException::new);

        this.projectRepository.delete(persistenceProject);
    }
}
