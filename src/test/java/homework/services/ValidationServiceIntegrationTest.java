package homework.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import homework.domains.LoanScheduler;
import homework.exceptions.CompanyInBlacklistException;
import homework.exceptions.MissedDataException;
import homework.exceptions.ValidationFailedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ValidationServiceIntegrationTest {


    @Autowired
    private ValidationServiceImpl validationService;

    @TestConfiguration
    static class ValidationServiceServiceTestContextConfiguration {

        @Bean
        public ValidationServiceImpl validationService() {
            return new ValidationServiceImpl();
        }
    }

    private LoanApplication loanApplication;

    @Value("classpath:loan_scheduler.json")
    Resource resultFile;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    @Before
    public void setUp() throws ParseException {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(formatter);
        Company company = new Company().builder().registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").build();
        loanApplication = new LoanApplication().builder().id(1L)
                .loanAmount(400.00).company(company).term((short) 12)
                .yearlyTurnover(3500.00)
                .confirmationDate(formatter.parse("2019-05-30"))
                .status(LoanApplicationStatus.APPLIED).build();

    }

    @Test
    public void validateLoanApplication_givenApplication_shouldPassWithoutExceptions() {
        validationService.validateLoanApplication(loanApplication);
    }

    @Test(expected = MissedDataException.class)
    public void validateLoanApplication_givenWrongApplication_throwMissedDataException() {
        loanApplication.setYearlyTurnover(null);
        validationService.validateLoanApplication(loanApplication);

    }

    @Test(expected = CompanyInBlacklistException.class)
    public void validateLoanApplication_givenWrongApplication_CompanyInBlacklistException() {
        loanApplication.getCompany().setBlacklisted(true);
        validationService.validateLoanApplication(loanApplication);

    }

    @Test(expected = ValidationFailedException.class)
    public void validateLoanApplication_givenWrongApplication_ValidationFailedException() {
        loanApplication.setLoanAmount(5000.00);
        validationService.validateLoanApplication(loanApplication);

    }


    @Test
    public void calculateLoanScheduler_givenApplication_shouldBeSameAsResourceFile() throws IOException {
        File result = resultFile.getFile();
        String json = new String(Files.readAllBytes(result.toPath()));
        LoanScheduler testResult = validationService.calculateLoanScheduler(loanApplication);
        LoanScheduler shouldBe = objectMapper.readValue(json, LoanScheduler.class);
        assertThat(testResult.getAmount())
                .isEqualTo(shouldBe.getAmount());
        assertThat(formatter.format(testResult.getConfirmationDate()))
                .isEqualTo(formatter.format(shouldBe.getConfirmationDate()));
        assertThat(testResult.getMonthlyPayments().size())
                .isEqualTo(shouldBe.getMonthlyPayments().size());

        IntStream.range(0, shouldBe.getMonthlyPayments().size()).forEach(i -> {
            assertThat(testResult.getMonthlyPayments().get(i).getCommission())
                    .isEqualTo(shouldBe.getMonthlyPayments().get(i).getCommission());
            assertThat(testResult.getMonthlyPayments().get(i).getPrincipal())
                    .isEqualTo(shouldBe.getMonthlyPayments().get(i).getPrincipal());
            assertThat(formatter.format(testResult.getMonthlyPayments().get(i).getTermDate()))
                    .isEqualTo(formatter.format(shouldBe.getMonthlyPayments().get(i).getTermDate()));

        });


    }
}


