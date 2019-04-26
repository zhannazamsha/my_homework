package homework.services;

import com.google.common.collect.Lists;
import homework.models.LoanApplication;
import homework.repositories.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    public LoanApplication applyApplication(LoanApplication loanApplication) {
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
}
