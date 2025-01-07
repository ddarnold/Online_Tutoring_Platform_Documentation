package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Transfer object representing an affiliation. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AffiliationTO {

  private Long affiliationId;

  @NotEmpty(message = "The affiliation type cannot be empty")
  private String affiliationType;

  @NotEmpty(message = "The university cannot be empty")
  private String universityName;
}
