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

    @Override
    public boolean isCompanyBlacklisted(LoanApplication loanApplication) {
        Optional<Company> company = companyRepository
                .findByRegistrationNumber(loanApplication.getCompany().getRegistrationNumber());
        return (company.isPresent() ? company.get().isBlacklisted() : false);
    }

    public void blacklistCheck(Company company) {
        if (loanApplicationRepository.findByCompanyAndDate(company,
                DateConversions.localDateToDate(LocalDateTime.now().minusMinutes(1).toLocalDate()))
                .size() > 2) {
            company.setBlacklisted(true);
            companyRepository.save(company);
        }
    }


}
