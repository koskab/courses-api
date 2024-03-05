package kz.alabs.academy.users.service.impl;

import kz.alabs.academy.core.exception.BadRequestException;
import kz.alabs.academy.core.exception.NotFoundException;
import kz.alabs.academy.core.security.JwtService;
import kz.alabs.academy.types.*;
import kz.alabs.academy.users.entity.UserEntity;
import kz.alabs.academy.users.mapper.UserMapper;
import kz.alabs.academy.users.repository.UserRepository;
import kz.alabs.academy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    @Override
    public UserEntity getEntityByEmail(String email) {
        return repository.findByEmail(email).
                orElseThrow(() -> new NotFoundException("User with such email not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserEntity getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with such id not found"));
    }

    @Transactional
    @Override
    public UserCreatePayload create(UserCreateInput userCreate) {
        validateCreationUser(userCreate);
        userCreate.setPassword(passwordEncoder.encode(userCreate.getPassword()));
        UserEntity user = UserMapper.INSTANCE.toEntity(userCreate);
        user = repository.save(user);

        return new UserCreatePayload(user.getId());
    }

    @Transactional
    @Override
    public UserUpdatePayload update(Long id, UserUpdateInput userUpdate) {
        validateUpdateUser(id, userUpdate);
        UserEntity requestedUser = getEntityById(id);
        userUpdate.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        UserEntity user = UserMapper.INSTANCE.toEntity(requestedUser, userUpdate);
        user = repository.save(user);

        return new UserUpdatePayload(user.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public User getCurrentProfile() {
        var user = getCurrentUser();

        return UserMapper.INSTANCE.toView(user);
    }

    @Transactional
    @Override
    public UserSignInPayload authenticate(UserSignInInput request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = getEntityByEmail(request.getEmail());
        var jwtToken = jwtService.generateToken(user);

        return new UserSignInPayload(jwtToken);
    }

    @Transactional(readOnly = true)
    @Override
    public UserEntity getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        return getEntityByEmail(username);
    }

    private void validateCreationUser(UserCreateInput userCreate) {
        Period period = Period.between(userCreate.getBirthdate(), LocalDate.now());

        if (repository.existsByEmail(userCreate.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        if (!userCreate.getPassword().equals(userCreate.getRePassword())) {
            throw new BadRequestException("Passwords don't match");
        }

        if (period.getYears() < 14) {
            throw new BadRequestException("Incorrect date of birth");
        }
    }

    private void validateUpdateUser(Long id, UserUpdateInput userUpdate) {
        UserEntity currentUser = getCurrentUser();

        if (!currentUser.getId().equals(id)) {
            throw new BadRequestException("Forbidden");
        }

        Period period = Period.between(userUpdate.getBirthdate(), LocalDate.now());

        if (!userUpdate.getPassword().equals(userUpdate.getRePassword())) {
            throw new BadRequestException("Passwords don't match");
        }

        if (period.getYears() < 14) {
            throw new BadRequestException("Incorrect date of birth");
        }
    }

}
