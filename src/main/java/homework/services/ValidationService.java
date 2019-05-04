package homework.services;

import homework.domains.LoanApplication;
import homework.domains.LoanScheduler;

public interface ValidationService {

    void validateLoanApplication(LoanApplication loanApplication);

    LoanScheduler calculateLoanScheduler(LoanApplication loanApplication);
}
