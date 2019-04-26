package homework.api;

import homework.models.LoanApplication;
import homework.services.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @PostMapping("/applyloan")
    public ResponseEntity<ApplyLoanResult> applyLoan(@RequestBody LoanApplication loanApplication) {
        loanApplicationService.applyApplication(loanApplication);
        ApplyLoanResult bookingResult = ApplyLoanResult.builder()
                .success(true)
                .build();
        return ResponseEntity.ok(bookingResult);
    }

    @GetMapping("/allapplications")
    public ResponseEntity<List<LoanApplication>> loadAllApplications() {
        List<LoanApplication> allApplications = loanApplicationService.loadAll();
        return ResponseEntity.ok(allApplications);
    }
}
