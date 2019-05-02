package homework.services;

import homework.exceptions.ApplicationNotFoundException;
import homework.exceptions.CompanyInBlacklistException;
import homework.exceptions.CompanyNotFoundException;
import homework.models.Company;
import homework.models.LoanApplication;
import homework.models.LoanApplicationStatus;
import homework.models.LoanScheduler;
import homework.repositories.CompanyRepository;
import homework.repositories.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private BlacklistService blacklistService;
    @Autowired
    private ValidationService validationService;

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

    public List<LoanApplication> findByRegistrationNum(String registrationNumber) {
        Optional<Company> company = companyRepository.findByRegistrationNumber(registrationNumber);
        company.orElseThrow(() -> new CompanyNotFoundException("Company not found"));
        return loanApplicationRepository.findByCompany(company.get());
    }

    @Override
    public LoanScheduler confirmApplication(Long id) {
        Optional<LoanApplication> loanApplication = loanApplicationRepository.findById(id);
        loanApplication.orElseThrow(() -> new ApplicationNotFoundException("Loan application not found"));
        LoanApplication foundLoanApplication = loanApplication.get();
        if (!foundLoanApplication.getStatus().equals(LoanApplicationStatus.CONFIRMED)) {
            validationService.validateLoanApplication(foundLoanApplication);
            foundLoanApplication.setConfirmationDate(new Date());
            foundLoanApplication.setStatus(LoanApplicationStatus.CONFIRMED);
            loanApplicationRepository.save(foundLoanApplication);
        }
        return validationService.calculateLoanScheduler(foundLoanApplication);
    }

    public List<LoanApplication> loadAll() {
        return loanApplicationRepository.findAllNonBlocklisted();
    }

    public LoanApplication rejectApplication(Long id) {
        Optional<LoanApplication> loanApplication = loanApplicationRepository.findById(id);
        loanApplication.orElseThrow(() -> new ApplicationNotFoundException("Loan application not found"));
        LoanApplication foundLoanApplication = loanApplication.get();
        foundLoanApplication.setStatus(LoanApplicationStatus.REJECTED);
        loanApplicationRepository.save(foundLoanApplication);
        return foundLoanApplication;
    }

    private void setDefaultValues(LoanApplication loanApplication) {
        loanApplication.setStatus(LoanApplicationStatus.APPLIED);
        loanApplication.setCreationDate(new Date());
        Optional<Short> optional = Optional.ofNullable(loanApplication.getTerm());
        loanApplication.setTerm(optional.orElse(LoanApplication.DEFAULT_TERM_VALUE));
    }


}
