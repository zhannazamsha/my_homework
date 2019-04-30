package homework.services;

import com.google.common.collect.Lists;
import homework.exceptions.CompanyInBlacklistException;
import homework.models.LoanApplication;
import homework.models.LoanApplicationStatus;
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
    BlacklistService blacklistService;

    public LoanApplication applyApplication(LoanApplication loanApplication) {
        if (blacklistService.isCompanyBlacklisted(loanApplication))
            throw new CompanyInBlacklistException("Company is in Blacklist");
        setDefaultValues(loanApplication);
        loanApplicationRepository.save(loanApplication);
        return loanApplication;
    }

    public LoanApplication findByRegistrationNum(String registrationNumber) {
        return loanApplicationRepository.findByCompanyRegistrationNum(registrationNumber);
    }

    public List<LoanApplication> loadAll() {
        Iterable<LoanApplication> result = loanApplicationRepository.findAll();
        return Lists.newArrayList(result);
    }

    public LoanApplication rejectApplication(String registrationNumber) {
        LoanApplication loanApplication = findByRegistrationNum(registrationNumber);
        loanApplication.setStatus(LoanApplicationStatus.REJECTED);
        loanApplicationRepository.save(loanApplication);
        return loanApplication;
    }

    private void setDefaultValues(LoanApplication loanApplication) {
        loanApplication.setStatus(LoanApplicationStatus.APPLIED);
        loanApplication.setCreationDate(new Date());
        Optional<Short> optional = Optional.ofNullable(loanApplication.getTerm());
        loanApplication.setTerm(optional.orElse(LoanApplication.DEFAULT_TERM_VALUE));
    }


}
