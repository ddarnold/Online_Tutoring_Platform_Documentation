package de.thu.thutorium.service;

import de.thu.thutorium.model.Category;
import de.thu.thutorium.repository.CategoryRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing category operations.
 * <p>
 * This class handles the business logic associated with the Category entity, acting
 * as an intermediary between the controller and the repository.
 * <p>
 * It leverages CategoryRepository to perform database operations such as fetching all
 * categories along with their associated courses.
 */
@Service
public class CategoryService {

    /**
     * Injecting the repository to perform CRUD operations on categories.
     */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Fetches all categories along with their associated courses.
     * <p>
     * This method calls the custom query from the CategoryRepository to fetch categories
     * and their courses, ensuring that the full list is returned.
     *
     * @return List of categories with associated courses.
     */
    public List<Category> getAllCategoriesWithCourses() {
        return categoryRepository.findAllCategoriesWithCourses();
    }
}
