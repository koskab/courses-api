package kz.alabs.academy.courses.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import kz.alabs.academy.DgsConstants;
import kz.alabs.academy.courses.service.CourseService;
import kz.alabs.academy.types.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

@DgsComponent
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation(field = DgsConstants.MUTATION.CreateCourse)
    public CourseCreatePayload create(@InputArgument CourseEditInput input) {
        return service.create(input);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation(field = DgsConstants.MUTATION.UpdateCourse)
    public CourseUpdatePayload update(@InputArgument Long id, @InputArgument CourseEditInput input) {
        return service.update(id, input);
    }

    @DgsQuery(field = DgsConstants.QUERY.Course)
    public Course findById(@InputArgument Long id) {
        return service.findById(id);
    }

    @DgsQuery(field = DgsConstants.QUERY.Courses)
    public Page<Course> findAllPageable(
            @InputArgument int page,
            @InputArgument int size,
            @InputArgument CourseSearch search) {
        return service.findAllPageable(page, size, search);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsMutation(field = DgsConstants.MUTATION.SignUpUser)
    public SignUpPayload signUp(@InputArgument Long courseId){
        return service.signUp(courseId);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsMutation(field = DgsConstants.MUTATION.SignOffUser)
    public SignOffPayload signOff(@InputArgument Long courseId) { return service.signOff(courseId); }


    @PreAuthorize("hasRole('ADMIN')")
    @DgsMutation(field = DgsConstants.MUTATION.DeleteCourse)
    public CourseDeletePayload delete(@InputArgument Long id) {
        return service.delete(id);
    }

}
