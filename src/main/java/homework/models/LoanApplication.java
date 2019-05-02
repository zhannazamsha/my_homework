package homework.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplication implements Serializable {

    public static final short DEFAULT_TERM_VALUE = 6;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Loan amount is empty")
    @Column(nullable = false)
    private Float loanAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    private Company company;

    private Float yearlyTurnover;

    @Min(value = 1, message = "Term is out of range (1-12) ")
    @Max(value = 12, message = "Term is out of range (1-12) ")
    @Column(nullable = false)
    private Short term;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoanApplicationStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date creationDate;

    private Date confirmationDate;

}
