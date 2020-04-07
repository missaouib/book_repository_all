package am.egs.bookRepository.exception;


public class DuplicateUserException extends RuntimeException {
    private String message;

    public DuplicateUserException (final String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
