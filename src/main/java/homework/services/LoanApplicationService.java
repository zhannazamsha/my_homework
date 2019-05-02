package homework.services;

import homework.models.LoanApplication;
import homework.models.LoanScheduler;

import java.util.List;

public interface LoanApplicationService {
    LoanApplication applyApplication(LoanApplication loanApplication);

    List<LoanApplication> loadAll();

    LoanApplication rejectApplication(Long id);

    List<LoanApplication> findByRegistrationNum(String registrationNumber);

    LoanScheduler confirmApplication(Long id);

}
