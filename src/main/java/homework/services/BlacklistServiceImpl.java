package homework.services;

import homework.models.LoanApplication;
import org.springframework.stereotype.Service;

@Service
public class BlacklistServiceImpl implements BlacklistService {
    @Override
    public boolean isCompanyBlacklisted(LoanApplication loanApplication) {
        return false;
    }
}
