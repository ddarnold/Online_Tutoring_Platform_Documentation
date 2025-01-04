package de.thu.thutorium.api.transferObjects.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressTO {

    private Long addressId;

    /**
     * Name of the campus.
     */
    @NotNull
    private UniversityTO university;

    /**
     * Name of the campus.
     */
    private String campusName;

    /**
     * House number.
     */
    @NotNull
    private String houseNum;

    /**
     * Name of the street.
     */
    @NotEmpty(message = "The street name cannot be empty")
    private String streetName;

    /**
     * City of the address.
     */
    @NotEmpty(message = "The city cannot be empty")
    private String city;

    /**
     * Postal code of the address.
     */
    @NotNull
    private String postalCode;

    /**
     * Country of the address.
     */
    @NotEmpty(message = "The country cannot be empty")
    private String country;

    /**
     * Phone number associated with the address.
     */
    @Size(max = 25, message = "Phone number cannot exceed 25 characters")
    private String phoneNumber;

    /**
     * Fax number associated with the address.
     */
    @Size(max = 25, message = "Fax number cannot exceed 25 characters")
    private String faxNumber;

    /**
     * Email address associated with the address.
     */
    @Email(message = "Email should be valid")
    private String emailAddress;
}
