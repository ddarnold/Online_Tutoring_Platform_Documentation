package de.thu.thutorium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sample")
public class SampleController {
    @Autowired
    private SampleService sampleService;
    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
    public ResponseEntity<List<SampleEntity>> getAllSamples() {
        return new ResponseEntity<List<SampleEntity>>(sampleService.allEntities(), HttpStatus.OK);
    }
}
