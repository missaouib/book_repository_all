package am.egs.bookRepository.controller;

import am.egs.bookRepository.model.User;
import am.egs.bookRepository.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static am.egs.bookRepository.util.Constant.ADMIN;
import static am.egs.bookRepository.util.Constant.READ;

@Controller
@RequestMapping(value = ADMIN)
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
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
    public ModelAndView deleteUser(@PathVariable Long id) {
        logger.info(" ADMIN getting delete user.");

        userService.delete(id);
        ModelAndView modelAndView = new ModelAndView();
        final List<User> userList = userService.findAllUsers();

        modelAndView.addObject("users", userList);
        modelAndView.addObject("process", "SUCCESS");
        modelAndView.addObject("pw_success", "Well done! You successfully  delete this user.");
        modelAndView.setViewName("users-list");
        logger.info(" You successfully delete this user.");

//        return "redirect:/admin/read";
        return modelAndView;

    }
}
