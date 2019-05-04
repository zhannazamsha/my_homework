package homework.services;

import homework.domains.LoanApplication;

import java.util.List;

public interface ApplyLoanApplicationService {

    LoanApplication applyApplication(LoanApplication loanApplication);

    List<LoanApplication> loadAll();

}
