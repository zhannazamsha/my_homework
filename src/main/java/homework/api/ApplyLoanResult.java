package homework.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyLoanResult {
    private boolean success;
}