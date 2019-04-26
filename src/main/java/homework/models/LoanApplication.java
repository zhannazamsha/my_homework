package homework.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LoanApplication implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private Float loanAmount;

    @Column(nullable = false, unique = true)
    private String companyRegistrationNum;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private Float yearlyTurnover;

    @Column(nullable = false)
    private Byte term;

    private String companyName;

    private String companyType;

    @Enumerated(EnumType.STRING)
    private LoanApplicationStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date creationDate;

    private Date confirmationDate;

}
