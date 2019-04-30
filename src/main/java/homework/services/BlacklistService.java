package homework.services;

import homework.models.LoanApplication;

public interface BlacklistService {
    boolean isCompanyBlacklisted(LoanApplication loanApplication);
}
