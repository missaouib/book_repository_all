package am.egs.socialSyte.payload.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Objects;

@Builder
public class AuthResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "message='" + message +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResponse that = (AuthResponse) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
