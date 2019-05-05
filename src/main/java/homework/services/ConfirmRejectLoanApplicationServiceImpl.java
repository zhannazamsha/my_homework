package homework.services;

import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import homework.domains.LoanScheduler;
import homework.domains.repositories.LoanApplicationRepository;
import homework.exceptions.ApplicationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ConfirmRejectLoanApplicationServiceImpl implements ConfirmRejectLoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private ValidationService validationService;


    @Override
    public LoanScheduler confirmApplication(Long id) {
        Optional<LoanApplication> loanApplication = loanApplicationRepository.findByIdForStatusChange(id, LoanApplicationStatus.APPLIED);
        loanApplication.orElseThrow(() -> new ApplicationNotFoundException("Loan application not found"));
        LoanApplication foundLoanApplication = loanApplication.get();
        if (!foundLoanApplication.getStatus().equals(LoanApplicationStatus.CONFIRMED)) {
            validationService.validateLoanApplication(foundLoanApplication);
            foundLoanApplication.setConfirmationDate(new Date());
            foundLoanApplication.setStatus(LoanApplicationStatus.CONFIRMED);
            loanApplicationRepository.save(foundLoanApplication);
        }
        return validationService.calculateLoanScheduler(foundLoanApplication);
    }


    public LoanApplication rejectApplication(Long id) {
        Optional<LoanApplication> loanApplication = loanApplicationRepository.findByIdForStatusChange(id, LoanApplicationStatus.APPLIED);
        loanApplication.orElseThrow(() -> new ApplicationNotFoundException("Loan application not found"));
        LoanApplication foundLoanApplication = loanApplication.get();
        foundLoanApplication.setStatus(LoanApplicationStatus.REJECTED);
        loanApplicationRepository.save(foundLoanApplication);
        return foundLoanApplication;
    }


}
