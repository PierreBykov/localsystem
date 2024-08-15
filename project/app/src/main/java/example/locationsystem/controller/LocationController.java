package com.example.locationsystem.controller;

import com.example.locationsystem.model.AccessLevel;
import com.example.locationsystem.model.Location;
import com.example.locationsystem.model.User;
import com.example.locationsystem.service.LocationService;
import com.example.locationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Location> createLocation(@RequestParam String name, @RequestParam String address, @RequestParam String ownerEmail) {
        User owner = userService.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));
        Location location = locationService.createLocation(name, address, owner);
        return ResponseEntity.ok(location);
    }

    @PostMapping("/{locationId}/share")
    public ResponseEntity<Void> shareLocation(@PathVariable Long locationId, @RequestParam String userEmail, @RequestParam AccessLevel accessLevel) {
        Location location = locationService.getLocationsByOwner(null).stream()
                .filter(loc -> loc.getId().equals(locationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Location not found"));
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        locationService.shareLocation(location, user, accessLevel);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{locationId}/friends")
    public ResponseEntity<List<User>> getFriendsOnLocation(@PathVariable Long locationId) {
        Location location = locationService.getLocationsByOwner(null).stream()
                .filter(loc -> loc.getId().equals(locationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Location not found"));
        List<User> friends = locationService.getFriendsOnLocation(location);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<Location>> getAllLocationsForUser(@PathVariable String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Location> locations = locationService.getAllLocationsForUser(user);
        return ResponseEntity.ok(locations);
    }

    @PostMapping("/{locationId}/manage-access")
    public ResponseEntity<Void> manageAccess(@PathVariable Long locationId, @RequestParam String userEmail, @RequestParam AccessLevel accessLevel) {
        Location location = locationService.getLocationsByOwner(null).stream()
                .filter(loc -> loc.getId().equals(locationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Location not found"));
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        locationService.manageAccess(location, user, accessLevel);
        return ResponseEntity.ok().build();
    }
}
