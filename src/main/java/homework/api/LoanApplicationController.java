package homework.api;

import homework.domains.LoanApplication;
import homework.domains.LoanScheduler;
import homework.services.ApplyLoanApplicationService;
import homework.services.ConfirmRejectLoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LoanApplicationController {

    @Autowired
    private ConfirmRejectLoanApplicationService confirmRejectLoanApplicationService;
    @Autowired
    private ApplyLoanApplicationService applyLoanApplicationService;

    @PostMapping("/applyloan")
    public ResponseEntity<LoanApplication> applyLoan(@Valid @RequestBody LoanApplication loanApplicationFormData) {
        LoanApplication loanApplication = applyLoanApplicationService.applyApplication(loanApplicationFormData);
        return ResponseEntity.ok(loanApplication);
    }

    @GetMapping("/allapplications")
    public ResponseEntity<List<LoanApplication>> loadAllApplications() {
        List<LoanApplication> allApplications = applyLoanApplicationService.loadAll();
        return ResponseEntity.ok(allApplications);
    }

    @PostMapping("/reject")
    public ResponseEntity<LoanApplication> rejectApplication(@Valid @RequestBody Long id) {
        LoanApplication loanApplication = confirmRejectLoanApplicationService.rejectApplication(id);
        return ResponseEntity.ok(loanApplication);
    }

    @PostMapping("/confirm")
    public ResponseEntity<LoanScheduler> confirmApplication(@Valid @RequestBody Long id) {
        LoanScheduler loanScheduler = confirmRejectLoanApplicationService.confirmApplication(id);
        return ResponseEntity.ok(loanScheduler);
    }
}
