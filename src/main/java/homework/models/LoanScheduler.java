package homework.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanScheduler implements Serializable {

    public static final float INTEREST_RATE = 0.03f;

    private double amount;
    private double interestRate;
    private short term;
    private double totalPrincipal;
    private double totalCommission;
    private Date confirmationDate;
    private List<MonthlyPayment> monthlyPayments;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class MonthlyPayment {
        private Date termDate;
        private double principal;
        private double commission;
    }


}
