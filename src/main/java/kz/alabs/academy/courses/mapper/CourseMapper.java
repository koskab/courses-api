package kz.alabs.academy.courses.mapper;

import kz.alabs.academy.courses.entity.CourseEntity;
import kz.alabs.academy.types.Course;
import kz.alabs.academy.types.CourseEditInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(builder = @Builder(disableBuilder = true))
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseEntity toEntity(CourseEditInput courseEdit);

    CourseEntity toEntity(@MappingTarget CourseEntity courseEntity, CourseEditInput courseEdit);

    Course toView(CourseEntity courseEntity);

}
