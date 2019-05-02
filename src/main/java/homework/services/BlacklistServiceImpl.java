package homework.services;

import homework.models.Company;
import homework.models.LoanApplication;
import homework.repositories.CompanyRepository;
import homework.repositories.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
                convertToDateViaInstant(LocalDateTime.now().minusMinutes(1).toLocalDate()))
                .size() > 2) {
            company.setBlacklisted(true);
            companyRepository.save(company);

        }
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
