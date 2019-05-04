package homework.domains.repositories;

import homework.domains.Company;
import homework.domains.LoanApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {

    List<LoanApplication> findByCompany(Company company);

    @Query("select a from LoanApplication a where a.company = :company and a.creationDate >= :creationDateTime")
    List<LoanApplication> findByCompanyAndDate(@Param("company") Company company,
                                               @Param("creationDateTime") Date creationDateTime);


    @Query("select a from LoanApplication a where a.company.blacklisted = false")
    List<LoanApplication> findAllNonBlacklisted();

}
