package homework.api;

import homework.models.LoanApplication;
import homework.services.LoanApplicationService;
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
    private LoanApplicationService loanApplicationService;

    @PostMapping("/applyloan")
    public ResponseEntity<LoanApplication> applyLoan(@Valid @RequestBody LoanApplication loanApplicationFormData) {
        LoanApplication loanApplication = loanApplicationService.applyApplication(loanApplicationFormData);
        return ResponseEntity.ok(loanApplication);
    }

    @GetMapping("/allapplications")
    public ResponseEntity<List<LoanApplication>> loadAllApplications() {
        List<LoanApplication> allApplications = loanApplicationService.loadAll();
        return ResponseEntity.ok(allApplications);
    }

    @PostMapping("/reject")
    public ResponseEntity<LoanApplication> rejectApplication(@Valid @RequestBody Long id) {
        LoanApplication loanApplication = loanApplicationService.rejectApplication(id);
        return ResponseEntity.ok(loanApplication);
    }
}
