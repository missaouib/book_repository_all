//package am.egs.socialSyte.payload.auth;
//
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//
//public class SignInRequest {
//    @Email
//    @NotNull(message = "{NotNull.email}")
//    private String email;
//
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
//    @NotNull(message = "{NotNull.password}")
//    private String password;
//
//    public SignInRequest() {
//    }
//
//    public SignInRequest(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//}
//
