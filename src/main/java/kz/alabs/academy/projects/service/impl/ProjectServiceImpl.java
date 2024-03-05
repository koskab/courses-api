package kz.alabs.academy.projects.service.impl;

import kz.alabs.academy.core.exception.BadRequestException;
import kz.alabs.academy.core.exception.NotFoundException;
import kz.alabs.academy.projects.entity.ProjectEntity;
import kz.alabs.academy.projects.mapper.ProjectMapper;
import kz.alabs.academy.projects.repository.ProjectRepository;
import kz.alabs.academy.projects.service.ProjectService;
import kz.alabs.academy.types.*;
import kz.alabs.academy.users.entity.UserEntity;
import kz.alabs.academy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;
    private final UserService userService;

    @Override
    @Transactional
    public ProjectCreatePayload create(ProjectEditInput input) {
        UserEntity user = userService.getCurrentUser();
        if(repository.existsByAuthor_Id(user.getId()))
            throw new BadRequestException("You can't have more than one project");

        if(StringUtils.isBlank(input.getName()) || StringUtils.isBlank(input.getDescription()))
            throw new BadRequestException("Project name and description cannot be empty");

        ProjectEntity entity = ProjectMapper.INSTANCE.toEntity(input);
        entity.setAuthor(user);
        entity = repository.save(entity);

        return new ProjectCreatePayload(entity.getId());
    }


    @Override
    @Transactional
    public ProjectUpdatePayload update(Long id, ProjectEditInput input) {

        if(StringUtils.isBlank(input.getName()) || StringUtils.isBlank(input.getDescription()))
            throw new BadRequestException("Project name and description cannot be empty");

        ProjectEntity entity = getEntityById(id);
        if(!entity.getAuthor().getId().equals(userService.getCurrentUser().getId()))
            throw new AccessDeniedException("Forbidden");

        entity = ProjectMapper.INSTANCE.toEntity(entity, input);
        entity = repository.save(entity);

        return new ProjectUpdatePayload(entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectEntity getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }

    @Override
    @Transactional
    public ProjectDeletePayload delete(Long id) {
        ProjectEntity entity = getEntityById(id);

        if(!entity.getAuthor().getId().equals(userService.getCurrentUser().getId()))
            throw new AccessDeniedException("Forbidden");

        repository.deleteById(id);

        return new ProjectDeletePayload(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Project findById(Long id) {
        ProjectEntity entity = getEntityById(id);
        return ProjectMapper.INSTANCE.toView(entity);
    }

}
