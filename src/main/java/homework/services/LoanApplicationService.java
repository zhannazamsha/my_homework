package homework.services;

import homework.models.LoanApplication;

import java.util.List;

public interface LoanApplicationService {
    LoanApplication applyApplication(LoanApplication loanApplication);

    List<LoanApplication> loadAll();

    LoanApplication rejectApplication(String registrationNum);

    LoanApplication findByRegistrationNum(String registrationNumber);
}
