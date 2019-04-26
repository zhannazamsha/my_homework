package homework.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import homework.models.LoanApplication;
import homework.models.LoanApplicationStatus;
import net.bytebuddy.utility.RandomString;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.RandomAccess;


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
        assertThat(loanApplication).hasFieldOrPropertyWithValue("email", "mail@mail.lv");
    }

    @Test
    public void findLoanApplication_findByCompanyRegistrationNumber_returnExistingLoanApplication() {
        LoanApplication loanApplication = buildLoanApplicationObject();
        entityManager.persist(loanApplication);
        entityManager.flush();
        LoanApplication found = loanApplicationRepository
                .findByCompanyRegistrationNum(loanApplication.getCompanyRegistrationNum());
        assertThat(found.getCompanyRegistrationNum())
                .isEqualTo(loanApplication.getCompanyRegistrationNum());
    }

    @Test
    public void loadAllLoanApplications_repositoryIsEmpty_shouldNotFindAny() {
        Iterable<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        assertThat(loanApplications).isEmpty();
    }

    @Test
    public void loadAllLoanApplications_twoEntitiesAdded_shouldBeFound() {
        LoanApplication loanApplication = buildLoanApplicationObject();
        entityManager.persist(loanApplication);
        entityManager.flush();
        Iterable<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        assertThat(loanApplications).isNotEmpty();
    }


    private LoanApplication buildLoanApplicationObject() {
        return new LoanApplication().builder()
                .loanAmount(10000f)
                .companyRegistrationNum(RandomString.make(10))
                .email("mail@mail.lv")
                .phone("324535")
                .yearlyTurnover(100f)
                .term((byte) 5)
                .status(LoanApplicationStatus.APPLIED).build();
    }

}
