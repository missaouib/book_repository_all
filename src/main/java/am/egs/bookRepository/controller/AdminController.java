package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.UserMapper;
import am.egs.bookRepository.model.User;
import am.egs.bookRepository.payload.UserDto;
import am.egs.bookRepository.repository.UserRepository;
import am.egs.bookRepository.security.UserPrincipal;
import am.egs.bookRepository.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

import static am.egs.bookRepository.util.Constant.ADMIN;
import static am.egs.bookRepository.util.Constant.READ;

@Controller
@RequestMapping(value = ADMIN)
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository, UserMapper userMapper) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping(READ)
    public String readUsersList(Model model) {
        logger.info(" ADMIN getting users read list of all users.");
        final List<User> userList = userService.findAllUsers();
        model.addAttribute("users", userList);
        logger.info(" ADMIN successful read list of all users.");
        return "users-list";
    }

    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        logger.info(" User successfully deleted.");
        return "redirect:/admin/read";
    }

    @RequestMapping(value = "/admin-Profile", method = RequestMethod.GET)
    public ModelAndView currentAdminName(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getUsername();
        userService.signInSuccess(email);
        User user = userRepository.findUserByEmail(email);
        UserDto userDto = userMapper.map(user, UserDto.class);
        String name  = userDto.getName();
        String surName  = userDto.getSurName();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userDto);
        modelAndView.addObject("name", name);
        modelAndView.addObject("tab",  " ");
        modelAndView.addObject("surname", surName);
        modelAndView.setViewName("admin-profile");
        return modelAndView;
    }
}
