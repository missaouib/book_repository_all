package am.egs.bookRepository.controller;

import am.egs.bookRepository.mappers.UserMapper;
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
    public String update(UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
        userService.updateUser(userDto, principal);
        logger.info(" User account was successful updated.");
        return "redirect:/userPage/profile";
    }
}
