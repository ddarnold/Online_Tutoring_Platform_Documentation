package de.thu.thutorium.database.repositories;

import de.thu.thutorium.database.dbObjects.AddressDBO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link AddressDBO} entities in the database. This
 * interface extends {@link JpaRepository}, providing CRUD operations and additional query methods
 * for the {@code AddressDBO} entity.
 *
 * <p>By extending {@code JpaRepository}, this interface inherits these methods and can be used to
 * perform database operations on {@code AddressDBO} entities.
 *
 * <p>Todo: Handle whitepaces in the fields (possibly with JPQL queries)
 */
public interface AddressRepository extends JpaRepository<AddressDBO, Long> {
    Optional<AddressDBO> findByHouseNumAndStreetNameAndPostalCodeAndCountryContainsIgnoreCase(
            String houseNum, String streetName, String postalCode, String country);

    Optional<AddressDBO>
    findByHouseNumAndStreetNameIgnoreCaseAndPostalCodeAndCountryIgnoreCaseAndUniversity_UniversityNameIgnoreCase(
            @NotNull String houseNum,
            @NotEmpty(message = "The street name cannot be empty") String streetName,
            @NotNull String postalCode,
            @NotEmpty(message = "The country cannot be empty") String country,
            @NotEmpty(message = "The university name cannot be empty") String universityName);

    List<AddressDBO> findByAddressId(Long addressId);
}
