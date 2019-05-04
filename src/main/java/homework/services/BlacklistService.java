package homework.services;

import homework.domains.Company;
import homework.domains.LoanApplication;

public interface BlacklistService {
    boolean isCompanyBlacklisted(LoanApplication loanApplication);

    void blacklistCheck(Company company);
}
