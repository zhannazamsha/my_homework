package homework.services;

import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import homework.domains.repositories.CompanyRepository;
import homework.domains.repositories.LoanApplicationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class BlacklistServiceTest {

    @Autowired
    private BlacklistServiceImpl blacklistService;

    @TestConfiguration
    static class BlacklistServiceTestContextConfiguration {

        @Bean
        public BlacklistServiceImpl blacklistService() {
            return new BlacklistServiceImpl();
        }
    }

    @MockBean
    private LoanApplicationRepository loanApplicationRepository;

    @MockBean
    private CompanyRepository companyRepository;

    private Company company;


    @Before
    public void setUp() {
        company = new Company().builder().registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").build();
        LoanApplication loanApplication = new LoanApplication().builder().id(1L)
                .loanAmount(10000.00).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();

        Mockito.when(companyRepository
                .findByRegistrationNumber(loanApplication.getCompany().getRegistrationNumber()))
                .thenReturn(java.util.Optional.ofNullable(loanApplication.getCompany()));

    }

    @Test
    public void blacklisttCheck_CompanyShouldBeInBlacklist() {
        LoanApplication loanApplication = new LoanApplication().builder().id(1L)
                .loanAmount(10000.00).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        loanApplication.getCompany().setBlacklisted(true);
        boolean isBlacklisted = blacklistService.isCompanyBlacklisted(loanApplication);
        assertThat(isBlacklisted)
                .isEqualTo(true);
    }


}
