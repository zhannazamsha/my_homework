package homework.services;

import homework.models.Company;
import homework.models.LoanApplication;
import homework.models.LoanApplicationStatus;
import homework.repositories.CompanyRepository;
import homework.repositories.LoanApplicationRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class BlacklistServiceIntegrationTest {

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
                .loanAmount(10000f).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        LoanApplication loanApplication2 = new LoanApplication().builder().id(2L)
                .loanAmount(20000f).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        List<LoanApplication> loanApplications = Lists.newArrayList(loanApplication, loanApplication2);
        Mockito.when(loanApplicationRepository.findByCompanyAndDate(company, new Date()))
                .thenReturn(loanApplications);
        Mockito.when(companyRepository
                .findByRegistrationNumber(loanApplication.getCompany().getRegistrationNumber()))
                .thenReturn(java.util.Optional.ofNullable(loanApplication.getCompany()));

    }


    @Test
    public void blacklistrCheck_CompanyShouldBeInBlacklist() {
        LoanApplication loanApplication = new LoanApplication().builder().id(1L)
                .loanAmount(10000f).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        loanApplication.getCompany().setBlacklisted(true);
        boolean isBlacklisted = blacklistService.isCompanyBlacklisted(loanApplication);
        assertThat(isBlacklisted)
                .isEqualTo(true);
    }

}
