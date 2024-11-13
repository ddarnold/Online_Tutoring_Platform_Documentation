package de.thu.thutorium.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long user_id;

  @Column(name = "first_name", nullable = false)
  private String first_name;

  @Column(name = "last_name", nullable = false)
  private String last_name;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "is_verified", nullable = false)
  private Boolean is_verified = false;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

}
