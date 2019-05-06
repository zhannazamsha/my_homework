package homework.services;


import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import homework.domains.LoanScheduler;
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
public class ConfirmRejectLoanApplicationServiceTest {


    @Autowired
    private ConfirmRejectLoanApplicationServiceImpl loanApplicationService;

    @TestConfiguration
    static class ConfirmRejectLoanApplicationServiceTestContextConfiguration {

        @Bean
        public ConfirmRejectLoanApplicationServiceImpl confirmRejectLoanApplicationService() {
            return new ConfirmRejectLoanApplicationServiceImpl();
        }
    }

    @MockBean
    private LoanApplicationRepository loanApplicationRepository;

    @MockBean
    private ValidationService validationService;


    @Before
    public void setUp() {
        Company company = new Company().builder().registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").blacklisted(false).build();
        LoanApplication loanApplication = new LoanApplication().builder().id(1L)
                .loanAmount(10000.00).company(company).term((short) 5).status(LoanApplicationStatus.APPLIED).build();
        List loanApplications = Lists.newArrayList(loanApplication);

        Mockito.when(loanApplicationRepository.findByIdForStatusChange(1L, LoanApplicationStatus.APPLIED))
                .thenReturn(java.util.Optional.ofNullable(loanApplication));
        Mockito.when(loanApplicationRepository.findAllNonBlacklisted()).thenReturn(loanApplications);
        Mockito.when(validationService.calculateLoanScheduler(loanApplication)).thenReturn(new LoanScheduler());
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
    public void confirmApplication_ShouldBeStatusConfirmed() {
        LoanScheduler found = loanApplicationService.confirmApplication(1L);
        assertThat(found)
                .isNotEqualTo(null);

    }


}
