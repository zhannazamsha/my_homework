package homework.services;

import homework.models.Company;
import homework.models.LoanApplication;

public interface BlacklistService {
    boolean isCompanyBlacklisted(LoanApplication loanApplication);

    void blacklistCheck(Company company);
}
