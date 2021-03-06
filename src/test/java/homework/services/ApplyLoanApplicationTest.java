package homework.services;

import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import homework.domains.repositories.CompanyRepository;
import homework.domains.repositories.LoanApplicationRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ApplyLoanApplicationTest {

    @Autowired
    private ApplyLoanApplicationServiceImpl applyLoanApplicationService;

    @TestConfiguration
    static class ApplyLoanApplicationServiceTestContextConfiguration {

        @Bean
        public ApplyLoanApplicationServiceImpl applyLoanApplicationService() {
            return new ApplyLoanApplicationServiceImpl();
        }
    }

    @MockBean
    private LoanApplicationRepository loanApplicationRepository;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private BlacklistService blacklistService;



    @Before
    public void setUp() {
        Company company = new Company().builder().registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").blacklisted(false).build();
        LoanApplication loanApplication = new LoanApplication().builder().id(1L)
                .loanAmount(10000.00).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        List loanApplications = Lists.newArrayList(loanApplication);

        Mockito.when(loanApplicationRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(loanApplication));
        Mockito.when(loanApplicationRepository.findAllNonBlacklisted()).thenReturn(loanApplications);


    }


    @Test
    public void loadAll_ShouldNotBeEmpty() {
        Iterable<LoanApplication> found = applyLoanApplicationService.loadAll();
        assertThat(Lists.newArrayList(found).isEmpty())
                .isEqualTo(false);
    }
}
