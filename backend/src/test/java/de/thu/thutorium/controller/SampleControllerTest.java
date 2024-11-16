package de.thu.thutorium.controller;

import de.thu.thutorium.model.SampleEntity;
import de.thu.thutorium.service.SampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SampleControllerTest {

    @Mock
    private SampleService sampleService;

    @InjectMocks
    private SampleController sampleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSamples() {
        // Arrange
        SampleEntity entity1 = new SampleEntity(1, "John", "Doe");
        SampleEntity entity2 = new SampleEntity(2, "Jane", "Smith");
        List<SampleEntity> sampleEntities = Arrays.asList(entity1, entity2);

        when(sampleService.allEntities()).thenReturn(sampleEntities);

        // Act
        ResponseEntity<List<SampleEntity>> response = sampleController.getAllSamples();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleEntities, response.getBody());
    }

    @Test
    void testGetAllSamples_WhenServiceReturnsEmptyList() {
        // Arrange
        when(sampleService.allEntities()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<SampleEntity>> response = sampleController.getAllSamples();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testGetAllSamples_WhenServiceThrowsException() {
        // Arrange
        when(sampleService.allEntities()).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        try {
            sampleController.getAllSamples();
        } catch (RuntimeException e) {
            assertEquals("Unexpected error", e.getMessage());
        }
    }
}