package am.egs.socialSite.exception;

public class EmailNotVerifiedException extends RuntimeException {
    private String message;

    public EmailNotVerifiedException (final String message) {
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
