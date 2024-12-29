package de.thu.thutorium.database.DBOMappers;

import de.thu.thutorium.api.transferObjects.CourseTO;
import de.thu.thutorium.database.dbObjects.CourseCategoryDBO;
import de.thu.thutorium.database.dbObjects.CourseDBO;
import de.thu.thutorium.database.dbObjects.RoleDBO;
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
 * Mapper interface for converting between {@link CourseTO} and {@link CourseDBO}.
 *
 * <p>This interface uses MapStruct to map data transfer objects (DTOs) to database objects (DBOs)
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

        //check if the user exists with TUTOR role from its ID
        RoleDBO tutorRole = roleRepository.findByRoleName(Role.TUTOR);
        Set<RoleDBO> tutorRoles = new HashSet<>();
        tutorRoles.add(tutorRole);
        if (!userRepository.existsByUserIdAndRolesContaining(course.getTutorId(), tutorRoles)) {
            throw new EntityNotFoundException("Tutor not found with id " + course.getTutorId());
        }

        //Throw error if the associated course categories are not found; else fetch those categories
        if (course.getCourseCategories() != null) {
            courseCategories = course.getCourseCategories().stream()
                    .map(categoryTO -> categoryRepository.findCourseCategoryDBOByCategoryName(categoryTO.getCategoryName())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryTO.getCategoryName())))
                    .toList();
        }

        return CourseDBO.builder()
                .courseName(course.getCourseName())
                .tutor(userRepository.findUserDBOByUserId(course.getTutorId()).get())
                .descriptionShort(course.getDescriptionShort())
                .descriptionLong(course.getDescriptionLong())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .courseCategories(courseCategories)
                .build();
    }
}
