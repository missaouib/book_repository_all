//package am.egs.socialSyte.payload.auth;
//
//import am.egs.socialSyte.validator.UniqueEmail;
//
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Size;
//
//public class SignUpRequest {
//    @Email(message = "{Email.email }")
//    @UniqueEmail(message = "{Unique.email}")
//    @NotNull(message = "{NotNull.email}}")
//    private String email;
//
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
//            message = "{Pattern.password}}")
//    @NotNull(message = "{NotNull.password}}")
//    private String password;
//
//    @Size(min = 3, max = 50, message = "{Size.name}}")
//    @NotNull(message = "{NotNull.name}")
//    private String name;
//
//    public SignUpRequest() {
//    }
//
//    public SignUpRequest(String email, String password, String name) {
//        this.email = email;
//        this.password = password;
//        this.name = name;
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
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}
