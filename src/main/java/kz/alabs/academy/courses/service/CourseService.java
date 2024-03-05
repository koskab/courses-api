package kz.alabs.academy.courses.service;

import kz.alabs.academy.courses.entity.CourseEntity;
import kz.alabs.academy.types.*;
import org.springframework.data.domain.Page;

public interface CourseService {

    CourseCreatePayload create(CourseEditInput input);

    CourseUpdatePayload update(Long id, CourseEditInput input);

    Page<Course> findAllPageable(int page, int size, CourseSearch search);

    Course findById(Long id);

    SignUpPayload signUp(Long courseId);

    SignOffPayload signOff(Long courseId);

    CourseEntity getEntityById(Long id);

    CourseDeletePayload delete(Long id);

}
