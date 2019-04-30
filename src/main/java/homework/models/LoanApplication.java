package homework.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LoanApplication implements Serializable {

    public static final short DEFAULT_TERM_VALUE = 6;

    @Id
    @GeneratedValue
    private long id;

    @NotNull(message = "Loan amount is empty")
    @Column(nullable = false)
    private Float loanAmount;

    @NotBlank(message = "Company registration number is empty")
    @Column(nullable = false, unique = true)  //, unique = true
    private String companyRegistrationNum;

    @NotBlank(message = "Email is empty")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Phone number is empty")
    @Column(nullable = false)
    private String phone;

    private Float yearlyTurnover;

    @Min(value = 1, message = "Term is out of range (1-12) ")
    @Max(value = 12, message = "Term is out of range (1-12) ")
    @Column(nullable = false)
    private Short term;

    private String companyName;

    private String companyType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoanApplicationStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date creationDate;

    private Date confirmationDate;

}
