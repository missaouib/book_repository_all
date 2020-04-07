package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.UserMapper;
import am.egs.bookRepository.model.Role;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.UserDto;
import am.egs.bookRepository.payload.UserEditForm;
import am.egs.bookRepository.security.UserPrincipal;
import am.egs.bookRepository.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static am.egs.bookRepository.util.Constant.UPDATE;
import static am.egs.bookRepository.util.Constant.USER;

@Controller
@RequestMapping(value = USER)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

//start>>> for update using modal

//    @RequestMapping("/edit")
//    @ResponseBody
//    public UserDto editUsersById(Long id) {
//        User user = userService.getUserById(id);
//        UserDto userDto = userMapper.map(user, UserDto.class);
//        return userDto;
//    }

//    @PostMapping(UPDATE)
//    public ModelAndView update(UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
//        logger.info(" User getting update profile.");
//        userService.updateUser(userDto, principal);
//        ModelAndView modelAndView = new ModelAndView();
//        String profile = showProfileDependenceFromRole(principal);
//        modelAndView.setViewName(profile);
//        User user = getUser(principal);
//        UserDto userDto1 = userMapper.map(user,UserDto.class);
//        modelAndView.addObject("user",userDto1);
//        modelAndView.addObject("name", userDto1.getName());
//        modelAndView.addObject("tab", " ");
//        modelAndView.addObject("surname", userDto1.getSurName());
//        modelAndView.addObject("process", "SUCCESS");
//        modelAndView.addObject("pw_success", "Well done! You successfully  updated  profile.");
//        logger.info(" You successfully updated  profile.");
//        return modelAndView;
//    }
//end>>> for update using modal


    @RequestMapping("/edit")
    @ResponseBody
    public ModelAndView showUpdateForm(Long id,@AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getUserById(id);
        UserEditForm userEditForm = userMapper.map(user, UserEditForm.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userEditForm);
        modelAndView.addObject("control", showRole(principal));
        modelAndView.setViewName("user_edit");
        return modelAndView;
    }

    @PostMapping(UPDATE)
    public ModelAndView update(  @AuthenticationPrincipal UserPrincipal principal,
                          @Valid @ModelAttribute("user")  UserEditForm userEditForm,
                               BindingResult result) {
        logger.info(" User getting update profile.");
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("user", userEditForm);
            modelAndView.setViewName("user_edit");
            return modelAndView;
        }
        userService.updateUser(userEditForm, principal);
        String profile = showProfileDependenceFromRole(principal);
        modelAndView.setViewName(profile);
        User user = getUser(principal);
        UserDto userDto = userMapper.map(user, UserDto.class);
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("name", userDto.getName());
        modelAndView.addObject("tab", " ");
        modelAndView.addObject("surname", userDto.getSurName());
        modelAndView.addObject("process", "SUCCESS");
        modelAndView.addObject("pw_success", "Well done! You successfully  updated  profile.");
        logger.info(" You successfully updated  profile.");
        return modelAndView;
    }

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
