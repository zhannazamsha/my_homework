package homework.repositories;

import homework.models.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findByRegistrationNumber(String registrationNum);
}
