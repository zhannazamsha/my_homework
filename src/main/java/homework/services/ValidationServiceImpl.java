package homework.services;

import homework.exceptions.CompanyInBlacklistException;
import homework.exceptions.MissedDataException;
import homework.exceptions.ValidationFailedException;
import homework.models.LoanApplication;
import homework.models.LoanScheduler;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {


    @Override
    public void validateLoanApplication(LoanApplication loanApplication) {
        if (loanApplication.getCompany().isBlacklisted()) {
            throw new CompanyInBlacklistException("Validation failed. Company is in Blacklist.");
        }
        if (loanApplication.getYearlyTurnover().isNaN()) {
            throw new MissedDataException("Yearly Turnover is not set");
        }
        double maxAllowedMonthlyLimit = (loanApplication.getYearlyTurnover() / 12) * 0.3;
        if (calculateMonthlyExpenses(loanApplication.getLoanAmount(),
                loanApplication.getTerm(), LoanScheduler.INTEREST_RATE) > maxAllowedMonthlyLimit) {
            throw new ValidationFailedException("Monthly expenses of loan is greater than max allowed monthly limit");
        }
    }

    private double calculateMonthlyExpenses(double amount, short term, float interest) {
        double monthPrinciple = amount / term;
        double monthCommission = amount * interest;
        return monthCommission + monthPrinciple;

    }

    public LoanScheduler calculateLoanScheduler(LoanApplication loanApplication) {
        LoanScheduler loanScheduler = new LoanScheduler().builder()
                .amount(loanApplication.getLoanAmount())
                .term(loanApplication.getTerm())
                .confirmationDate(loanApplication.getConfirmationDate())
                .interestRate(LoanScheduler.INTEREST_RATE).build();
        double monthlyExpense = calculateMonthlyExpenses(loanApplication.getLoanAmount(),
                loanApplication.getTerm(), LoanScheduler.INTEREST_RATE);


        return loanScheduler;

    }


}
