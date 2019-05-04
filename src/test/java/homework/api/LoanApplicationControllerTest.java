package homework.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import homework.domains.LoanScheduler;
import homework.services.ApplyLoanApplicationService;
import homework.services.ConfirmRejectLoanApplicationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoanApplicationController.class)
public class LoanApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = new ObjectMapper();


    @MockBean
    private ConfirmRejectLoanApplicationService confirmRejectLoanApplicationService;

    @MockBean
    private ApplyLoanApplicationService applyLoanApplicationService;

    private LoanApplication loanApplication;

    @Before
    public void setUp() {
        loanApplication = buildLoanApplicationTestObject();
    }


    @Test
    public void applyLoan_ReturnsHttpStatusOk() throws Exception {
        given(applyLoanApplicationService.applyApplication(loanApplication)).willReturn(loanApplication);
        System.out.println(objectMapper.writeValueAsString(loanApplication));
        mockMvc.perform(
                post("/applyloan").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanApplication)))
                .andExpect(status().isOk());
    }

    @Test
    public void loadAllApplications_OneObjectAdded_returnJson() throws Exception {
        given(applyLoanApplicationService.applyApplication(loanApplication)).willReturn(loanApplication);
        mockMvc.perform(
                get("/allapplications").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Lists.newArrayList(loanApplication))))
                .andExpect(status().isOk());
    }

    @Test
    public void rejectApplication_ReturnsHttpStatusOk() throws Exception {
        given(confirmRejectLoanApplicationService.rejectApplication(loanApplication.getId())).willReturn(loanApplication);
        mockMvc.perform(
                post("/reject").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanApplication.getId())))
                .andExpect(status().isOk());
    }

    @Test
    public void confirmApplication_ReturnsHttpStatusOk() throws Exception {
        LoanScheduler loanScheduler = new LoanScheduler();
        given(confirmRejectLoanApplicationService.confirmApplication(loanApplication.getId())).willReturn(loanScheduler);
        mockMvc.perform(
                post("/confirm").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanApplication.getId())))
                .andExpect(status().isOk());
    }



    private LoanApplication buildLoanApplicationTestObject() {
        Company company = new Company().builder().companyId(1L).registrationNumber("333444").email("mail@mail.lv")
                .phone("324535").build();
        return new LoanApplication().builder()
                .id(1L).loanAmount(10000.00).company(company).yearlyTurnover(100.00).term((short) 5).status(LoanApplicationStatus.APPLIED).build();

    }


}
