package homework.repositories;

import homework.models.LoanApplication;
import org.springframework.data.repository.CrudRepository;


public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {

    LoanApplication findByCompanyRegistrationNum(String registrationNum);
}
