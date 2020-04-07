package am.egs.socialSyte.validator;

import am.egs.socialSyte.repository.UserRepository;
import am.egs.socialSyte.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserService userService;
    private final UserRepository userRepository;

    public EmailValidator(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

//    @Override
//    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
//        if(userRepository.getUserByEmail(s)!=null)
//            return false;
//        else return true;
//    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userService.getUserByEmail(s) == null;
    }
}

