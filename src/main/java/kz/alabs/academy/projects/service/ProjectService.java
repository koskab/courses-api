package kz.alabs.academy.projects.service;

import kz.alabs.academy.projects.entity.ProjectEntity;
import kz.alabs.academy.types.*;

public interface ProjectService {

    ProjectCreatePayload create(ProjectEditInput input);

    ProjectUpdatePayload update(Long id, ProjectEditInput input);

    Project findById(Long id);

    ProjectEntity getEntityById(Long id);

    ProjectDeletePayload delete(Long id);

}
