package homework.services;

import homework.domains.Company;
import homework.domains.LoanApplication;
import homework.domains.LoanApplicationStatus;
import homework.domains.repositories.CompanyRepository;
import homework.domains.repositories.LoanApplicationRepository;
import homework.exceptions.CompanyInBlacklistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ApplyLoanApplicationServiceImpl implements ApplyLoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private BlacklistService blacklistService;


    public LoanApplication applyApplication(LoanApplication loanApplication) {
        if (blacklistService.isCompanyBlacklisted(loanApplication))
            throw new CompanyInBlacklistException("Company is in Blacklist");
        Optional<Company> company = companyRepository
                .findByRegistrationNumber(loanApplication.getCompany().getRegistrationNumber());
        loanApplication.setCompany(company.orElseGet(() -> companyRepository.save(loanApplication.getCompany())));
        setDefaultValues(loanApplication);
        loanApplicationRepository.save(loanApplication);
        blacklistService.blacklistCheck(loanApplication.getCompany());
        return loanApplication;
    }

    public List<LoanApplication> loadAll() {
        return loanApplicationRepository.findAllNonBlacklisted();
    }


    private void setDefaultValues(LoanApplication loanApplication) {
        loanApplication.setStatus(LoanApplicationStatus.APPLIED);
        loanApplication.setCreationDate(new Date());
    }
}
