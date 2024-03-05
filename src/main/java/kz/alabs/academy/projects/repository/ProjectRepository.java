package kz.alabs.academy.projects.repository;

import kz.alabs.academy.projects.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    boolean existsByAuthor_Id(Long authorId);
}
