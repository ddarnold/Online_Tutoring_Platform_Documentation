package de.thu.thutorium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {
    @Autowired
    private SampleRepository sampleRepository;

    public List<SampleEntity> allEntities(){ return (List<SampleEntity>) sampleRepository.findAll();}


}
