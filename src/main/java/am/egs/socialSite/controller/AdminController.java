package am.egs.socialSite.controller;

import am.egs.socialSite.model.User;
import am.egs.socialSite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static am.egs.socialSite.util.Constant.*;

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
        List<User> userList;
        userList = userService.findAllUsers();
        model.addAttribute("users", userList);
        logger.info(" ADMIN successful read list of all users.");

        return "list-users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        logger.info(" User successful deleted.");
        return "redirect:/admin/read";
//        return new ResponseEntity("User successful deleted", HttpStatus.OK);
    }



}
