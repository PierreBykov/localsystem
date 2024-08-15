package example.locationsystem

import example.locationsystem.controller.LocationController
import example.locationsystem.model.Location
import example.locationsystem.model.User
import example.locationsystem.service.LocationService
import example.locationsystem.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class LocationControllerSpec extends Specification {

    UserService userService = Mock()
    LocationService locationService = Mock()
    LocationController locationController = new LocationController(locationService, userService)

    def "should create a new location"() {
        given:
        String ownerEmail = "john.doe@example.com"
        String locationName = "Home"
        String address = "123 Main St, New York, NY"
        User user = new User("John Doe", ownerEmail)
        Location location = new Location(locationName, address, user)

        when:
        ResponseEntity<Location> response = locationController.createLocation(locationName, address, ownerEmail)

        then:
        1 * userService.findByEmail(ownerEmail) >> Optional.of(user)
        1 * locationService.createLocation(_ as Location) >> location
        response.statusCode == HttpStatus.CREATED
        response.body.name == locationName
        response.body.address == address
    }

    def "should share location with another user"() {
    given:
    Long locationId = 1L
    String friendEmail = "jane.doe@example.com"
    User owner = new User("John Doe", "john.doe@example.com")
    User friend = new User("Jane Doe", friendEmail)
    Location location = new Location("Home", "123 Main St, New York, NY", owner)

    when:
    ResponseEntity<Void> response = locationController.shareLocation(locationId, friendEmail, "READ_ONLY")

    then:
    1 * locationService.getLocationsByOwner(owner) >> [location]
    1 * userService.findByEmail(friendEmail) >> Optional.of(friend)
    1 * locationService.shareLocation(location, friend, "READ_ONLY")
    response.statusCode == HttpStatus.OK
                }

}
