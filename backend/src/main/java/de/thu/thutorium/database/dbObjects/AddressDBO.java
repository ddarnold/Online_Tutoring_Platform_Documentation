package de.thu.thutorium.database.dbObjects;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an address entity in the database. This class is mapped to the "address" table in the
 * database using JPA annotations.
 *
 * <p>The {@code Address} class includes attributes for a typical address such as house number,
 * street name, city, postal code, and country.
 *
 * <p>This class uses Lombok annotations to reduce boilerplate code for getter, setter, equals,
 * hashCode, and toString methods.
 *
 * <p>Annotations: - {@code @Entity} - Marks this class as a JPA entity. - {@code @Table(name =
 * "address")} - Maps this entity to the "address" table in the database. - {@code @Data} - Lombok
 * annotation to generate getters, setters, and other utility methods. - {@code @NoArgsConstructor}
 * - Lombok annotation to generate a no-argument constructor. - {@code @AllArgsConstructor} - Lombok
 * annotation to generate an all-argument constructor.
 *
 * <p>Attributes: - {@code addressId} - The primary key for the address, auto-generated. - {@code
 * houseNum} - The house number, must not be null. - {@code streetName} - The name of the street,
 * must not be null. - {@code city} - The name of the city, must not be null. - {@code postalCode} -
 * The postal code, must not be null. - {@code country} - The name of the country, must not be null.
 */
@Entity
@Table(name = "address")
@Getter
@Setter
@Builder
@AllArgsConstructor
@EntityListeners(DBPreProcess.class) // Save all string fields in all entities in lower case
public class AddressDBO {

  /**
   * The unique identifier for the address. This is the primary key and is auto-generated using
   * {@code GenerationType.IDENTITY}.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  @Setter(AccessLevel.NONE)
  private Long addressId;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "university")
  private UniversityDBO university;

  /** The campus name for the university. */
  @Column(name = "campus_name", length = 255)
  private String campusName;

  /** The house number for the address. This field is required and cannot be null. */
  @Column(name = "house_number", nullable = false)
  private String houseNum;

  /** The name of the street for the address. This field is required and cannot be null. */
  @Column(name = "street_name", nullable = false)
  private String streetName;

  /** The city for the address. This field is required and cannot be null. */
  @Column(name = "city", nullable = false)
  private String city;

  /** The postal code for the address. This field is required and cannot be null. */
  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  /** The country for the address. This field is required and cannot be null. */
  @Column(name = "country", nullable = false)
  private String country;

  /** The phone number of the address. */
  @Column(name = "phone_number")
  private String phoneNumber;

  /** The fax number of the address. */
  @Column(name = "fax_number")
  private String faxNumber;

  /** The email address of the address. */
  @Column(name = "email_address")
  private String emailAddress;

  /**
   * The meeting associated with this address.
   *
   * <p>Defines a bidirectional one-to-many relationship with {@link MeetingDBO}.
   */
  @OneToMany(mappedBy = "address")
  @Builder.Default
  private List<MeetingDBO> meetings = new ArrayList<>();

  /** Constructs an AddressDBO with an empty set of meetings . */
  public AddressDBO() {
    this.meetings = new ArrayList<>();
  }
}
