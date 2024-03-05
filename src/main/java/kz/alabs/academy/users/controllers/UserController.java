package kz.alabs.academy.users.controllers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import kz.alabs.academy.DgsConstants;
import kz.alabs.academy.types.*;
import kz.alabs.academy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

@DgsComponent
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DgsMutation(field = DgsConstants.MUTATION.CreateUser)
    public UserCreatePayload create(@InputArgument UserCreateInput input) {
        return userService.create(input);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsMutation(field = DgsConstants.MUTATION.UpdateUser)
    public UserUpdatePayload update(@InputArgument Long id, @InputArgument UserUpdateInput input) {
        return userService.update(id, input);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsQuery(field = DgsConstants.QUERY.CurrentUser)
    public User currentUser() {
        return userService.getCurrentProfile();
    }

    @DgsMutation(field = DgsConstants.MUTATION.Authenticate)
    public UserSignInPayload signIn(@InputArgument UserSignInInput input) {
        return userService.authenticate(input);
    }

    @PreAuthorize("isAuthenticated()")
    @DgsMutation(field = DgsConstants.MUTATION.Logout)
    public boolean logout() {
        SecurityContextHolder.clearContext();
        return true;
    }

}