package homework.services;

import homework.models.LoanApplication;
import homework.models.LoanScheduler;

public interface ValidationService {

    void validateLoanApplication(LoanApplication loanApplication);

    LoanScheduler calculateLoanScheduler(LoanApplication loanApplication);
}
