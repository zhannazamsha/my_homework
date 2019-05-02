package homework.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Company implements Serializable {

    @Id
    @GeneratedValue
    private Long companyId;

    @NotBlank(message = "Company registration number is empty")
    @Column(nullable = false, unique = true)  //, unique = true
    private String registrationNumber;

    @NotBlank(message = "Email is empty")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Phone number is empty")
    @Column(nullable = false)
    private String phone;

    private String companyName;

    private String companyType;

    @JsonIgnore
    private boolean blacklisted = false;


}
