package de.thu.thutorium.service;

import java.util.List;

import de.thu.thutorium.model.SampleEntity;
import de.thu.thutorium.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
  @Autowired private SampleRepository sampleRepository;

  public List<SampleEntity> allEntities() {
    return (List<SampleEntity>) sampleRepository.findAll();
  }
}
