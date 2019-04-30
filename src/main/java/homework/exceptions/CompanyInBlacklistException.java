package homework.exceptions;

public class CompanyInBlacklistException extends RuntimeException {
    public CompanyInBlacklistException(String message) {
        super(message);
    }
}
