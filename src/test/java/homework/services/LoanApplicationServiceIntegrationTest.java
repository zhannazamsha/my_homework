package homework.services;


import homework.models.LoanApplication;
import homework.models.LoanApplicationStatus;
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

    @TestConfiguration
    static class LoanApplicationServiceTestContextConfiguration {

        @Bean
        public LoanApplicationService employeeService() {
            return new LoanApplicationService();
        }
    }

    @Autowired
    private LoanApplicationService loanApplicationService;

    @MockBean
    private LoanApplicationRepository loanApplicationRepository;

    @Before
    public void setUp() {
        LoanApplication loanApplication = new LoanApplication().builder()
                .loanAmount(10000f).companyRegistrationNum("333444").email("mail@mail.lv")
                .phone("324535").yearlyTurnover(100f).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        List loanApplications = Lists.newArrayList(loanApplication);
        Mockito.when(loanApplicationRepository.findByCompanyRegistrationNum("333444"))
                .thenReturn(loanApplication);
        Mockito.when(loanApplicationRepository.findAll()).thenReturn(loanApplications);
    }

    @Test
    public void loanApplication_ShouldBeFoundByRegistrationNum() {
        String registrationNum = "333444";
        LoanApplication found = loanApplicationService.findByRegistrationNum(registrationNum);
        assertThat(found.getCompanyRegistrationNum())
                .isEqualTo(registrationNum);
    }

    @Test
    public void loadAll_ShouldNotBeEmpty() {
        Iterable<LoanApplication> found = loanApplicationService.loadAll();
        assertThat(Lists.newArrayList(found).isEmpty())
                .isEqualTo(false);
    }

}
