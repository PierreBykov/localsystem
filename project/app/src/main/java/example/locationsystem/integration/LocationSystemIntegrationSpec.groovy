package com.example.locationsystem.integration

import com.example.locationsystem.controller.LocationController
import com.example.locationsystem.controller.UserController
import com.example.locationsystem.model.Location
import com.example.locationsystem.model.User
import com.example.locationsystem.service.LocationService
import com.example.locationsystem.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import spock.lang.Specification

@SpringBootTest
class LocationSystemIntegrationSpec extends Specification {

    @Autowired
    UserController userController

    @Autowired
    LocationController locationController

    def "should create user and location and then share location"() {
        given:
        String ownerEmail = "john.doe@example.com"
        String friendEmail = "jane.doe@example.com"

        when: "register a new user"
        ResponseEntity<User> ownerResponse = userController.registerUser("John Doe", ownerEmail)
        ResponseEntity<User> friendResponse = userController.registerUser("Jane Doe", friendEmail)

        and: "create a new location"
        ResponseEntity<Location> locationResponse = locationController.createLocation("Home", "123 Main St, New York, NY", ownerEmail)

        and: "share location with a friend"
        ResponseEntity<Void> shareResponse = locationController.shareLocation(locationResponse.body.id, friendEmail, "READ_ONLY")

        then:
        ownerResponse.statusCode.value() == 201
        friendResponse.statusCode.value() == 201
        locationResponse.statusCode.value() == 201
        shareResponse.statusCode.value() == 200

        and: "verify that the friend has access to the location"
        ResponseEntity<List<User>> friendsResponse = locationController.getFriendsOnLocation(locationResponse.body.id)
        friendsResponse.body.size() == 1
        friendsResponse.body[0].email == friendEmail
    }
}
