package am.egs.socialSyte.controller;

import am.egs.socialSyte.model.Role;
import am.egs.socialSyte.model.User;
import am.egs.socialSyte.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin")
//@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AdminController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

//    @GetMapping("/read")
//    public ResponseEntity<List<User>> read() {
//        List<User> userList;
//        userList = userService.findAllUsers();
////        try {
////            userList = userService.findAllUsers();
////        } catch (UserNotFoundException e) {
////            return new ResponseEntity("Users not found.", HttpStatus.NOT_FOUND);
////        }
//        return ResponseEntity.ok(userList);
//    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<Object> deleteUser(@RequestParam String email) {
//        userService.delete(email);
////        try {
////            userService.delete(email);
////        } catch (UserNotFoundException e) {
////            return new ResponseEntity("There isn't user with this email.", HttpStatus.NOT_FOUND);
////        }
//        return new ResponseEntity("User successful deleted", HttpStatus.OK);
//    }
//
//    @GetMapping("/usersThatHaveThisRole")
//    public ResponseEntity<List<User>> findUserThatHaveThisRole(Role role) {
//        logger.info("Search users that have this role.");
//        List<User> userList = userService.userList(role);
//        return ResponseEntity.ok(userList);
//    }
}
