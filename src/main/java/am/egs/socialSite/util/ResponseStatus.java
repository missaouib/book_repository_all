package am.egs.socialSite.util;

public enum ResponseStatus {

    UNAUTHORIZED_REQUEST(401, "unauthorized.request", "unauthorized request"),
    USER_LOCKED(423, "user.locked", "locked"),
    INTERNAL_ERROR(500, "internal.error", "internal error"),
    USER_NOT_FOUND(601, "user.not.found", "user not found"),
    USER_BAD_CREDENTIALS(602, "user.bad.credentials", "authentication failed"),
    USER_AUTHENTICATION_ERROR(604, "user.auth.error", "auth error"),
    NOT_VERIFIED(999,"email.not.verified", "Email not verified");

  //  public static final String NOT_VERIFIED = "Email not verified";
    /**
     * Code of response
     */
    private final int code;

    /**
     * Message key
     */
    private final String messageKey;

    /**
     * Description of response
     */
    private final String description;

    /**
     * Constructor
     */
    ResponseStatus(final int code, String messageKey, final String description) {
        this.code = code;
        this.messageKey = messageKey;
        this.description = description;
    }

    /**
     * Gets Response Status by code
     */
    public static ResponseStatus valueOf(final int code) {

        for (ResponseStatus e : ResponseStatus.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }


    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the messageKey
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the string representation of this enum constant.
     *
     * @return the string representation of this enum constant.
     */
    @Override
    public String toString() {
        return description + "|" + messageKey + "|" + code + "|";
    }
}


