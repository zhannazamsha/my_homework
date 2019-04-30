package homework.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import homework.models.LoanApplicationStatus;
import homework.models.LoanApplication;
import homework.services.LoanApplicationServiceImpl;
import org.assertj.core.util.Lists;
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


    private ObjectMapper objectMapper = new ObjectMapper();


    @MockBean
    private LoanApplicationServiceImpl loanApplicationService;


    @Test
    public void applyLoan_ReturnsHttpStatusOk() throws Exception {
        LoanApplication loanApplication = buildLoanApplicationTestObject();
        given(loanApplicationService.applyApplication(loanApplication)).willReturn(loanApplication);
        System.out.println(objectMapper.writeValueAsString(loanApplication));
        mockMvc.perform(
                post("/applyloan").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanApplication)))
                .andExpect(status().isOk());
    }

    @Test
    public void loadAllApplications_OneObjectAdded_returnJson() throws Exception {
        LoanApplication loanApplication = buildLoanApplicationTestObject();
        given(loanApplicationService.applyApplication(loanApplication)).willReturn(loanApplication);
        mockMvc.perform(
                get("/allapplications").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Lists.newArrayList(loanApplication))))
                .andExpect(status().isOk());
    }

    @Test
    public void rejectApplication_ReturnsHttpStatusOk() throws Exception {
        LoanApplication loanApplication = buildLoanApplicationTestObject();
        given(loanApplicationService.rejectApplication(loanApplication.getCompanyName())).willReturn(loanApplication);
        mockMvc.perform(
                post("/reject").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanApplication)))
                .andExpect(status().isOk());
    }



    private LoanApplication buildLoanApplicationTestObject() {
        return new LoanApplication().builder()
                .loanAmount(10000f).companyRegistrationNum("333444").email("mail@mail.lv")
                .phone("324535").yearlyTurnover(100f).term((short) 5).status(LoanApplicationStatus.APPLIED).build();

    }


}
