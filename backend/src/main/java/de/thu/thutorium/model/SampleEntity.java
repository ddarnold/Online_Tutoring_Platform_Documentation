package de.thu.thutorium.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "actors")
@Table(name = "sample")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String first_name;
  private String last_name;
}
