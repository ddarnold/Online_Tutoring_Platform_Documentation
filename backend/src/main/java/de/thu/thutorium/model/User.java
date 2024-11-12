package de.thu.thutorium.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_account")
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String first_name;
  private String last_name;
  @Enumerated(EnumType.STRING)
  private UserRole role;
  private Boolean is_verified = false;

  // ...

}
