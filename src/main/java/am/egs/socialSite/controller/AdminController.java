package am.egs.socialSite.controller;

import am.egs.socialSite.model.User;
import am.egs.socialSite.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping(READ)
    public ResponseEntity<List<User>> readUsersList() {
        List<User> userList;
        userList = userService.findAllUsers();
        logger.info(" ADMIN successful read list of all users.");
        return ResponseEntity.ok(userList);
    }

    @DeleteMapping(DELETE)
    public ResponseEntity<Object> deleteUser(@RequestParam String email) {
        userService.delete(email);
        logger.info(" User successful deleted.");
        return new ResponseEntity("User successful deleted", HttpStatus.OK);
    }
}
