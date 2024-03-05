package kz.alabs.academy.users.service;

import kz.alabs.academy.types.*;
import kz.alabs.academy.users.entity.UserEntity;

public interface UserService {

    UserSignInPayload authenticate(UserSignInInput request);

    UserEntity getCurrentUser();

    UserEntity getEntityByEmail(String email);

    UserEntity getEntityById(Long id);

    UserCreatePayload create(UserCreateInput userCreate);

    UserUpdatePayload update(Long id, UserUpdateInput userUpdate);

    User getCurrentProfile();

}
