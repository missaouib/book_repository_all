package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.UserMapper;
import am.egs.bookRepository.model.Role;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.PassChangeForm;
import am.egs.bookRepository.payload.UserDto;
import am.egs.bookRepository.repository.UserRepository;
import am.egs.bookRepository.security.UserPrincipal;
import am.egs.bookRepository.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/userPage")
public class UserPageController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public UserPageController(UserService userService, AuthenticationManager authenticationManager, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView currentUserName(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        User user = userRepository.findUserByEmail(email);
        userService.signInSuccess(email);
        UserDto userDto = userMapper.map(user, UserDto.class);
        String name = userDto.getName();
        String surName = userDto.getSurName();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("name", name);
        modelAndView.addObject("tab", " ");
        modelAndView.addObject("surname", surName);
        List<Role> role = user.getRoles();
        if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
            modelAndView.setViewName("user-profile");
        } else {
            modelAndView.setViewName("admin-profile");
        }
        return modelAndView;
    }


    @RequestMapping("/change_pass")
    @ResponseBody
    public ModelAndView showPassChangeForm(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);
        PassChangeForm passChangeForm = userMapper.map(user, PassChangeForm.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", passChangeForm);
        modelAndView.addObject("control", showRole(principal));

        modelAndView.setViewName("change_pass");
        return modelAndView;
    }

    @RequestMapping(value = "/savePass_change", method = RequestMethod.POST)
    public ModelAndView confirmPasswordChange(@AuthenticationPrincipal UserPrincipal principal,
                                              @Valid @ModelAttribute("user") PassChangeForm passChangeForm, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (!passChangeForm.getNewPassword().equals(passChangeForm.getConfirmNewPassword())) {
            bindingResult.rejectValue("newPassword", " ", "*New password not matching");
            modelAndView.addObject("user", passChangeForm);
            modelAndView.addObject("control", showRole(principal));
            modelAndView.setViewName("change_pass");
            return modelAndView;
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("user", passChangeForm);
            modelAndView.addObject("control", showRole(principal));
            modelAndView.setViewName("change_pass");
            return modelAndView;
        }
        String email = principal.getUsername();
        User user = userService.findByEmail(email);
        if (!passwordEncoder.matches(passChangeForm.getOldPassword(), user.getPassword())) {
            modelAndView.addObject("user", passChangeForm);
            modelAndView.addObject("control", showRole(principal));
            modelAndView.setViewName("change_pass");
            bindingResult.rejectValue("oldPassword", " ", "*Check your old password!");
            return modelAndView;
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(passChangeForm.getNewPassword()));
            userService.save(user);
            UserDto userDto = userMapper.map(user, UserDto.class);
            modelAndView.addObject("user", userDto);
            modelAndView.setViewName(showProfileDependenceFromRole(principal));
            modelAndView.addObject("name", userDto.getName());
            modelAndView.addObject("tab", " ");
            modelAndView.addObject("surname", userDto.getSurName());
            modelAndView.addObject("process", "SUCCESS");
            modelAndView.addObject("pw_success", "Well done! You successfully change your password.");
        }
        return modelAndView;
    }

//for modal
//    @RequestMapping(value = "/savePass_change", method = RequestMethod.POST)
//    public ModelAndView confirmPasswordChange(@Valid UserDto userDto, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView();
//        String oldPassword = userService.getUserById(userDto.getId()).getPassword();
//        String postOldPassword = userDto.getName();
//        User user = userService.getUserById(userDto.getId());
//        String name = user.getName();
//        String surName = user.getSurName();
//        modelAndView.addObject("name", name);
//        modelAndView.addObject("tab", " ");
//        modelAndView.addObject("surname", surName);
//        List<Role> role = user.getRoles();
//        if (passwordEncoder.matches(postOldPassword, oldPassword)) {
//            user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
//            userService.save(user);
//            modelAndView.addObject("user", userService.getUserById(userDto.getId()));
//            modelAndView.addObject("process", "SUCCESS");
//            modelAndView.addObject("pw_success", "Well done! You successfully change your password.");
//            if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
//                modelAndView.setViewName("user-profile");
//            } else {
//                modelAndView.setViewName("admin-profile");
//            }
//        } else {
//            modelAndView.addObject("user", userService.getUserById(userDto.getId()));
//            modelAndView.addObject("process", "ERROR");
//            modelAndView.addObject("pw_error", "Error : Check your old password!");
////            modelAndView.addObject("rule", new User());
//            if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
//                modelAndView.setViewName("user-profile");
//            } else {
//                modelAndView.setViewName("admin-profile");
//            }
//        }
//        return modelAndView;
//    }

    public List<Role> getRole(UserPrincipal principal) {
        User user = getUser(principal);
        List<Role> role = user.getRoles();
        return role;
    }

    public User getUser(UserPrincipal principal) {
        String email = principal.getUsername();
        User user = userService.findByEmail(email);
        return user;
    }

    public String showProfileDependenceFromRole(UserPrincipal principal) {
        List<Role> role = getRole(principal);
        if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
            return "user-profile";
        } else
            return "admin-profile";
    }

    public String showRole(UserPrincipal principal) {
        List<Role> role = getRole(principal);
        if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
            return "USER";
        } else
            return "ADMIN";
    }
}
