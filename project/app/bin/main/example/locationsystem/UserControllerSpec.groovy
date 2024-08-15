package example.locationsystem

import example.locationsystem.controller.UserController
import example.locationsystem.model.User
import example.locationsystem.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class UserControllerSpec extends Specification {

    UserService userService = Mock()
    UserController userController = new UserController(userService)

    def "should register a new user"() {
        given:
        String email = "john.doe@example.com"
        String name = "John Doe"
        User user = new User(name, email)

        when:
        ResponseEntity<User> response = userController.registerUser(name, email)

        then:
        1 * userService.createUser(_ as User) >> user
        response.statusCode == HttpStatus.CREATED
        response.body.email == email
        response.body.name == name
    }
}
