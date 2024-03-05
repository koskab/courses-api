package kz.alabs.academy.projects.controllers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import kz.alabs.academy.DgsConstants;
import kz.alabs.academy.projects.service.ProjectService;
import kz.alabs.academy.types.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@DgsComponent
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @PreAuthorize("isAuthenticated()")
    @DgsMutation(field = DgsConstants.MUTATION.CreateProject)
    public ProjectCreatePayload create(@InputArgument ProjectEditInput input) {
        return service.create(input);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsMutation(field = DgsConstants.MUTATION.UpdateProject)
    public ProjectUpdatePayload update(@InputArgument Long id, @InputArgument ProjectEditInput input) {
        return service.update(id, input);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsMutation(field = DgsConstants.MUTATION.DeleteProject)
    public ProjectDeletePayload delete(@InputArgument Long id) {
        return service.delete(id);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsQuery(field = DgsConstants.QUERY.Project)
    public Project findById(@InputArgument Long id) {
        return service.findById(id);
    }
}
