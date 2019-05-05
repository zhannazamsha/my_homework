package homework.services;

import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.repositories.CompanyRepository;
import homework.domains.repositories.LoanApplicationRepository;
import homework.utils.DateConversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    private final static int MAX_ALLOWED_APPLIES_PER_MINUTE = 2;

    @Override
    public boolean isCompanyBlacklisted(LoanApplication loanApplication) {
        Optional<Company> company = companyRepository
                .findByRegistrationNumber(loanApplication.getCompany().getRegistrationNumber());
        return (company.isPresent() ? company.get().isBlacklisted() : false);
    }

    @Override
    public void blacklistCheck(Company company) {
        if (applyedWithinMinute(company) > MAX_ALLOWED_APPLIES_PER_MINUTE) {
            company.setBlacklisted(true);
            companyRepository.save(company);
        }
    }

    private int applyedWithinMinute(Company company) {
        return loanApplicationRepository.findByCompanyAndDate(company,
                DateConversions.localDateToDate(LocalDateTime.now().minusMinutes(1).toLocalDate())).size();
    }


}
