package kz.alabs.academy.courses.service.impl;

import jakarta.persistence.criteria.Predicate;
import kz.alabs.academy.core.exception.BadRequestException;
import kz.alabs.academy.core.exception.NotFoundException;
import kz.alabs.academy.courses.entity.CourseEntity;
import kz.alabs.academy.courses.mapper.CourseMapper;
import kz.alabs.academy.courses.repository.CourseRepository;
import kz.alabs.academy.courses.service.CourseService;
import kz.alabs.academy.types.*;
import kz.alabs.academy.users.entity.UserEntity;
import kz.alabs.academy.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;
    private final UserService userService;

    @Override
    @Transactional
    public CourseCreatePayload create(CourseEditInput input) {
        validateInput(input);

        CourseEntity entity = CourseMapper.INSTANCE.toEntity(input);
        entity = repository.save(entity);

        return new CourseCreatePayload(entity.getId());
    }

    @Override
    @Transactional
    public CourseUpdatePayload update(Long id, CourseEditInput input) {
        validateInput(input);

        CourseEntity entity = getEntityById(id);
        entity = CourseMapper.INSTANCE.toEntity(entity, input);
        entity = repository.save(entity);

        return new CourseUpdatePayload(entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Course> findAllPageable(int page, int size, CourseSearch search) {
        Specification<CourseEntity> where = buildSpecification(search);
        return repository.findAll(where, PageRequest.of(page, size)).map(CourseMapper.INSTANCE::toView);
    }

    @Override
    @Transactional(readOnly = true)
    public Course findById(Long id) {
        return CourseMapper.INSTANCE.toView(getEntityById(id));
    }

    @Override
    @Transactional
    public CourseEntity getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    @Override
    @Transactional
    public SignUpPayload signUp(Long courseId) {

        CourseEntity course = getEntityById(courseId);
        UserEntity user = userService.getCurrentUser();
        Set<UserEntity> students= course.getStudents();
        if(students.stream().anyMatch(u -> u.getId().equals(user.getId())))
            throw new BadRequestException("You are already enrolled in this course");

        students.add(user);

        return new SignUpPayload(courseId);
    }

    @Override
    @Transactional
    public SignOffPayload signOff(Long courseId) {

        CourseEntity course = getEntityById(courseId);
        UserEntity user = userService.getCurrentUser();
        Set<UserEntity> students= course.getStudents();
        if(students.stream().noneMatch(u -> u.getId().equals(user.getId())))
            throw new BadRequestException("You are not enrolled in this course");

        students.remove(user);

        return new SignOffPayload(courseId);
    }

    @Override
    @Transactional
    public CourseDeletePayload delete(Long courseId){
        if(!repository.existsById(courseId))
            throw new NotFoundException("Course not found");

        repository.deleteById(courseId);
        return new CourseDeletePayload(courseId);
    }

    private void validateInput(CourseEditInput input) {

        if(input.getDuration() < 1){
            throw new BadRequestException("Course duration cannot be less than 1 month");
        }

        if(StringUtils.isBlank(input.getName())) {
            throw new BadRequestException("Course name cannot be empty");
        }

        if(repository.existsByNameAndPriceAndDuration(input.getName(), input.getPrice(), input.getDuration())){
            throw new BadRequestException("Course with such properties already exists");
        }
    }

    private Specification<CourseEntity> buildSpecification(CourseSearch search) {

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.and();

            if (search == null)
                return predicate;

            boolean hasPriceFrom = search.getPriceFrom() != null;
            boolean hasPriceTo = search.getPriceTo() != null;
            boolean hasDurationFrom = search.getDurationFrom() != null;
            boolean hasDurationTo = search.getDurationTo() != null;
            validateInterval(search.getPriceFrom(), search.getPriceTo());
            validateInterval(search.getDurationFrom(), search.getDurationTo());


            if (StringUtils.isNotBlank(search.getName()))
                predicate = criteriaBuilder.and(predicate, criteriaBuilder
                        .like(criteriaBuilder
                                .lower(root.get("name")), "%" + search.getName().toLowerCase() + "%"));

            if (StringUtils.isNotBlank(search.getDescription()))
                predicate = criteriaBuilder.and(predicate, criteriaBuilder
                        .like(criteriaBuilder
                                .lower(root.get("description")), "%" + search.getDescription().toLowerCase() + "%"));

            if (hasPriceFrom)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .greaterThanOrEqualTo(root.get("price"), search.getPriceFrom()));

            if (hasPriceTo)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .lessThanOrEqualTo(root.get("price"), search.getPriceTo()));

            if (hasDurationFrom)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .greaterThanOrEqualTo(root.get("duration"), search.getDurationFrom()));

            if (hasDurationTo)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .lessThanOrEqualTo(root.get("duration"), search.getDurationTo()));

            return predicate;
        };
    }

    private void validateInterval(Number from, Number to){
        boolean hasFrom = from != null;
        boolean hasTo = to != null;

        if ((hasTo && hasFrom
                && from.doubleValue() > to.doubleValue())
                || (hasFrom && Math.signum(from.doubleValue()) == -1)
                || (hasTo && Math.signum(to.doubleValue()) == -1))
            throw new BadRequestException("Invalid interval input");
    }

}
