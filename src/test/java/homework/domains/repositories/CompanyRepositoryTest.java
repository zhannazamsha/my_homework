package homework.domains.repositories;

import homework.domains.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void saveCompany_saveCompnay_shouldBeSavedAndFoundById() {
        Company company = buildLoanApplicationObject();
        entityManager.persist(company);
        entityManager.flush();
        Optional<Company> companyFound = companyRepository.findById(company.getCompanyId());
        assertThat(companyFound.isPresent());
    }

    @Test
    public void findByRegistrationNumber_saveAndSearchByRegNumber_shouldBeFound() {
        Company company = buildLoanApplicationObject();
        entityManager.persist(company);
        entityManager.flush();
        Optional<Company> companyFound = companyRepository.findByRegistrationNumber(company.getRegistrationNumber());
        assertThat(companyFound.isPresent());
    }


    private Company buildLoanApplicationObject() {
        return new Company().builder().registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").build();

    }

}
