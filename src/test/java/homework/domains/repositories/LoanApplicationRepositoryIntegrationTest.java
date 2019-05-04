package homework.domains.repositories;

import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class LoanApplicationRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Test
    public void saveLoanApplication_saveApplication_shouldBeSaved() {
        LoanApplication loanApplication = buildLoanApplicationObject();
        entityManager.persist(loanApplication);
        entityManager.flush();
        Optional<LoanApplication> loanApplicationFound = loanApplicationRepository.findById(loanApplication.getId());
        assertThat(loanApplicationFound.isPresent());
    }

    @Test
    public void findLoanApplication_findByCompany_returnExistingLoanApplicationList() {
        LoanApplication loanApplication = buildLoanApplicationObject();
        entityManager.persist(loanApplication.getCompany());
        entityManager.persist(loanApplication);
        entityManager.flush();
        List<LoanApplication> found = loanApplicationRepository
                .findByCompany(loanApplication.getCompany());
        assertThat(found.get(0).getCompany().getRegistrationNumber())
                .isEqualTo(loanApplication.getCompany().getRegistrationNumber());
    }

    @Test
    public void loadAllLoanApplications_repositoryIsEmpty_shouldNotFindAny() {
        Iterable<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        assertThat(loanApplications).isEmpty();
    }

    @Test
    public void loadAllLoanApplications_oneEntityAdded_shouldBeFound() {
        LoanApplication loanApplication = buildLoanApplicationObject();
        entityManager.persist(loanApplication.getCompany());
        entityManager.persist(loanApplication);
        entityManager.flush();
        Iterable<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        assertThat(loanApplications).isNotEmpty();
    }

    @Test
    public void loadAllNonBlacklistedLoanApplications_oneBlockedEntityAdded_shouldNotBeFound() {
        LoanApplication loanApplication = buildLoanApplicationObject();
        loanApplication.getCompany().setBlacklisted(true);
        entityManager.persist(loanApplication.getCompany());
        entityManager.persist(loanApplication);
        entityManager.flush();
        Iterable<LoanApplication> loanApplications = loanApplicationRepository.findAllNonBlacklisted();
        assertThat(loanApplications).isEmpty();
    }

    @Test
    public void findByCompanyAndDate_twoEntitiesAdded_shouldBeFound() {
        LoanApplication loanApplication = buildLoanApplicationObject();
        entityManager.persist(loanApplication.getCompany());
        entityManager.persist(loanApplication);
        entityManager.flush();
        LoanApplication loanApplication2 = buildLoanApplicationObject();
        loanApplication2.setCompany(loanApplication.getCompany());
        entityManager.persist(loanApplication2);
        entityManager.flush();
        Iterable<LoanApplication> loanApplications = loanApplicationRepository
                .findByCompanyAndDate(loanApplication.getCompany(), new Date(System.currentTimeMillis() - 60 * 1000));
        assertThat(loanApplications).isNotEmpty();
    }



    private LoanApplication buildLoanApplicationObject() {
        Company company = new Company().builder().registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").build();
        return new LoanApplication().builder()
                .loanAmount(10000.00)
                .company(company)
                .yearlyTurnover(100.00)
                .term((short) 5)
                .status(LoanApplicationStatus.APPLIED)
                .creationDate(new Date())
                .build();
    }

}
