package de.thu.thutorium.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

  private Category category;

  @BeforeEach
  void setUp() {
    // Set up a sample category
    category = new Category();
    category.setCategoryId(1L);
    category.setCategoryName("Programming");
  }

  @Test
  void testCategoryFields() {
    assertEquals(1L, category.getCategoryId());
    assertEquals("Programming", category.getCategoryName());
  }

  @Test
  void testSettersAndGetters() {
    category.setCategoryName("Mathematics");
    assertEquals("Mathematics", category.getCategoryName());

    category.setCategoryId(2L);
    assertEquals(2L, category.getCategoryId());
  }

  @Test
  void testDefaultConstructor() {
    Category emptyCategory = new Category();
    assertNull(emptyCategory.getCategoryId());
    assertNull(emptyCategory.getCategoryName());
  }

  @Test
  void testAllArgsConstructor() {
    Category newCategory = new Category(2L, "Science");

    assertEquals(2L, newCategory.getCategoryId());
    assertEquals("Science", newCategory.getCategoryName());
  }

  @Test
  void testEquality() {
    Category anotherCategory = new Category(1L, "Programming");
    assertEquals(category, anotherCategory);
  }

  @Test
  void testHashCode() {
    Category anotherCategory = new Category(1L, "Programming");
    assertEquals(category.hashCode(), anotherCategory.hashCode());
  }

  @Test
  void testToString() {
    String expected = "Category(categoryId=1, categoryName=Programming)";
    assertEquals(expected, category.toString());
  }
}
