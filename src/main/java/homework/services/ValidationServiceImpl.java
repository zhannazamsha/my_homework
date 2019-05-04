package homework.services;

import homework.domains.LoanApplication;
import homework.domains.LoanScheduler;
import homework.exceptions.CompanyInBlacklistException;
import homework.exceptions.MissedDataException;
import homework.exceptions.ValidationFailedException;
import homework.utils.DateConversions;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


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
        return getMonthPrinciple(amount, term) + getMonthCommission(amount, interest);
    }

    private double getMonthCommission(double amount, float interest) {
        return amount * interest;
    }

    private double getMonthPrinciple(double amount, short term) {
        return amount / term;
    }

    public LoanScheduler calculateLoanScheduler(LoanApplication loanApplication) {
        LoanScheduler loanScheduler = new LoanScheduler().builder()
                .amount(loanApplication.getLoanAmount())
                .term(loanApplication.getTerm())
                .confirmationDate(loanApplication.getConfirmationDate())
                .interestRate(LoanScheduler.INTEREST_RATE).build();
        createMonthlyExpenseCalendar(loanApplication, loanScheduler);
        return loanScheduler;
    }

    private void createMonthlyExpenseCalendar(LoanApplication loanApplication, LoanScheduler loanScheduler) {
        double monthlyCommision = getMonthCommission(loanApplication.getLoanAmount(), LoanScheduler.INTEREST_RATE);
        double monthlyPrinciple = getMonthPrinciple(loanApplication.getLoanAmount(), loanApplication.getTerm());
        LocalDate paymentDateStart = DateConversions.dateToLocalDate(loanApplication.getConfirmationDate());
        for (int i = 0; i < loanApplication.getTerm(); i++) {
            loanScheduler.createMonthlyExpens(DateConversions.localDateToDate(paymentDateStart.plusMonths(i + 1))
                    , monthlyPrinciple, monthlyCommision);
        }
    }


}
