package de.thu.thutorium.controller;

import de.thu.thutorium.model.Category;
import de.thu.thutorium.service.CategoryService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling category-related requests.
 * <p>
 * This controller exposes endpoints for the frontend to interact with category data,
 * such as retrieving all categories and their associated courses.
 * <p>
 * It uses CategoryService to handle the logic and database interaction behind
 * these operations.
 */
@RestController
public class CategoryController {

    /**
     * Injecting the service responsible for category operations.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Endpoint to retrieve all categories along with their associated courses.
     * <p>
     * This method is mapped to the GET request at /search/categories-with-courses,
     * and it returns the full list of categories from the database.
     * <p>
     * CrossOrigin allows requests from the specified frontend URL.
     *
     * @return List of categories with associated courses.
     */
    @GetMapping("search/categories-with-courses")
    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
    public List<Category> getAllCategoriesWithCourses() {
        return categoryService.getAllCategoriesWithCourses();
    }
}
