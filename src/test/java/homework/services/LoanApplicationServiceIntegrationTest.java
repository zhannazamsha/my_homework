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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class LoanApplicationServiceIntegrationTest {


    @Autowired
    private LoanApplicationServiceImpl loanApplicationService;

    @TestConfiguration
    static class LoanApplicationServiceTestContextConfiguration {

        @Bean
        public LoanApplicationServiceImpl loanApplicationService() {
            return new LoanApplicationServiceImpl();
        }
    }

    @MockBean
    private LoanApplicationRepository loanApplicationRepository;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private BlacklistService blacklistService;

    @MockBean
    private ValidationService ValidationService;


    @Before
    public void setUp() {
        Company company = new Company().builder().registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").blacklisted(false).build();
        LoanApplication loanApplication = new LoanApplication().builder().id(1L)
                .loanAmount(10000.00).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        List loanApplications = Lists.newArrayList(loanApplication);

        Mockito.when(loanApplicationRepository.findById(1L))
                .thenReturn(java.util.Optional.ofNullable(loanApplication));
        Mockito.when(loanApplicationRepository.findAllNonBlocklisted()).thenReturn(loanApplications);


    }

    // @Test
    public void loanApplication_ShouldBeFoundByRegistrationNum() {
        String registrationNum = "333444";
        List<LoanApplication> found = loanApplicationService.findByRegistrationNum(registrationNum);
        assertThat(found.get(0).getCompany().getRegistrationNumber())
                .isEqualTo(registrationNum);
    }

    @Test
    public void rejectApplication_ShouldBeStatusRejected() {
        String registrationNum = "333444";
        LoanApplication found = loanApplicationService.rejectApplication(1L);
        assertThat(found.getCompany().getRegistrationNumber())
                .isEqualTo(registrationNum);
        assertThat(found.getStatus())
                .isEqualTo(LoanApplicationStatus.REJECTED);
    }

    @Test
    public void loadAll_ShouldNotBeEmpty() {
        Iterable<LoanApplication> found = loanApplicationService.loadAll();
        assertThat(Lists.newArrayList(found).isEmpty())
                .isEqualTo(false);
    }

}
