package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.common.CourseTO;
import de.thu.thutorium.database.dbObjects.CourseCategoryDBO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.UserDBO;
import de.thu.thutorium.database.dbObjects.enums.Role;
import de.thu.thutorium.database.repositories.CategoryRepository;
import de.thu.thutorium.database.repositories.RoleRepository;
import de.thu.thutorium.database.repositories.UserRepository;
import de.thu.thutorium.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Mapper class for converting between {@link CourseTO} and {@link CourseDBO}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CourseDBOMapper {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;

    /**
     * Maps a {@link CourseTO} object to a {@link CourseDBO} object.
     *
     * @param course the {@link CourseTO} object to map.
     * @return the mapped {@link CourseDBO} object.
     */
    public CourseDBO toDBO(CourseTO course) {
        List<CourseCategoryDBO> courseCategories = new ArrayList<>();
        Optional<UserDBO> tutor;

        //check if the user exists with TUTOR role from its ID
        //Todo:
        // Simplify with userRepository.findUserDBOByUserIdAndRoles_RoleName(course.getTutorId(), Role.TUTOR)??
        tutor = userRepository.findUserDBOByUserId(course.getTutorId());
        tutor.ifPresentOrElse(
                (user) -> {
                    boolean isTutor =  user.getRoles().stream().anyMatch(
                            role -> role.getRoleName().equals(Role.TUTOR));
                    if (!isTutor) {
                        throw new IllegalArgumentException("User with id " + course.getTutorId()
                                + " does not have tutor authorizations!");
                    }
                }, () -> {
                    throw new EntityNotFoundException("User not found with id " + course.getTutorId());
                }
        );

        //Throw error if the associated course categories are not found; else fetch those categories
        if (course.getCourseCategories() != null) {
            courseCategories = course.getCourseCategories().stream()
                    .map(categoryTO -> categoryRepository.findCourseCategoryDBOByCategoryName(categoryTO.getCategoryName())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryTO.getCategoryName())))
                    .toList();
        }

        return CourseDBO.builder()
                .courseName(course.getCourseName())
                .tutor(tutor.get())
                .descriptionShort(course.getDescriptionShort())
                .descriptionLong(course.getDescriptionLong())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .courseCategories(courseCategories)
                .build();
    }
}
