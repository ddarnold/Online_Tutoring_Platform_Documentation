package de.thu.thutorium;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
  @Autowired private SampleRepository sampleRepository;

  public List<SampleEntity> allEntities() {
    return (List<SampleEntity>) sampleRepository.findAll();
  }
}
