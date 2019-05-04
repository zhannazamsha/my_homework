package homework.services;

import homework.domains.LoanApplication;
import homework.domains.LoanScheduler;

public interface ConfirmRejectLoanApplicationService {

    LoanApplication rejectApplication(Long id);

    LoanScheduler confirmApplication(Long id);

}
