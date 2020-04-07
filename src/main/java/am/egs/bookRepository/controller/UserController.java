package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.UserMapper;
import am.egs.bookRepository.model.Role;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.UserDto;
import am.egs.bookRepository.security.UserPrincipal;
import am.egs.bookRepository.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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


    @RequestMapping("/edit")
    @ResponseBody
    public UserDto editUsersById(Long id) throws Exception {
        User user = userService.getUserById(id);
        UserDto userDto = userMapper.map(user, UserDto.class);
        return userDto;
    }


    @PostMapping(UPDATE)
    public ModelAndView update(UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
        logger.info(" User getting update profile.");
        userService.updateUser(userDto, principal);
        ModelAndView modelAndView = new ModelAndView();
        String profile = showRole(principal);
        modelAndView.setViewName(profile);
        User user = getUser(principal);
        UserDto userDto1 = userMapper.map(user,UserDto.class);
        modelAndView.addObject("user",userDto1);
        modelAndView.addObject("name", userDto1.getName());
        modelAndView.addObject("tab", " ");
        modelAndView.addObject("surname", userDto1.getSurName());
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

    public String showRole(UserPrincipal principal) {
        List<Role> role = getRole(principal);
        if (role.stream().map(Role::getRole).anyMatch("USER"::equals)) {
            return "user-profile";
        } else
            return "admin-profile";
    }

}
