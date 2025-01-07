package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/** Transfer object representing a university. */
public class UniversityTO {

  private Long universityId;

  @NotEmpty(message = "The university name cannot be empty")
  private String universityName;
}
