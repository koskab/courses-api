package kz.alabs.academy.projects.mapper;

import kz.alabs.academy.projects.entity.ProjectEntity;
import kz.alabs.academy.types.Project;
import kz.alabs.academy.types.ProjectEditInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface ProjectMapper {

    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectEntity toEntity(ProjectEditInput projectCreate);

    ProjectEntity toEntity(@MappingTarget ProjectEntity projectEntity, ProjectEditInput projectUpdate);

    Project toView(ProjectEntity projectEntity);

}
