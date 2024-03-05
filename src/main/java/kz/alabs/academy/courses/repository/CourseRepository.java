package kz.alabs.academy.courses.repository;

import kz.alabs.academy.courses.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {

    boolean existsByNameAndPriceAndDuration(String name, BigDecimal price, Integer duration);

}
