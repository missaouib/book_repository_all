package am.egs.socialSyte.controller;

import am.egs.socialSyte.model.User;
import am.egs.socialSyte.service.UserService;
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

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/read")
    public ResponseEntity<List<User>> readUsersList() {
        List<User> userList;
        userList = userService.findAllUsers();
        logger.info(" ADMIN successful read list of all users.");
        return ResponseEntity.ok(userList);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam String email) {
        userService.delete(email);
        logger.info(" User successful deleted.");
        return new ResponseEntity("User successful deleted", HttpStatus.OK);
    }
}
