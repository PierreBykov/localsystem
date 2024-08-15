package com.example.locationsystem.service;

import com.example.locationsystem.model.*;
import com.example.locationsystem.repository.LocationRepository;
import com.example.locationsystem.repository.LocationSharingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationSharingRepository locationSharingRepository;

    public Location createLocation(String name, String address, User owner) {
        Location location = new Location(name, address, owner);
        return locationRepository.save(location);
    }

    public List<Location> getLocationsByOwner(User owner) {
        return locationRepository.findByOwner(owner);
    }

    public void shareLocation(Location location, User user, AccessLevel accessLevel) {
        if (!locationSharingRepository.existsByLocationAndUser(location, user)) {
            LocationSharing locationSharing = new LocationSharing(location, user, accessLevel);
            locationSharingRepository.save(locationSharing);
        }
    }

    public List<User> getFriendsOnLocation(Location location) {
        return locationSharingRepository.findByLocation(location)
            .stream()
            .map(LocationSharing::getUser)
            .toList();
    }

    public List<Location> getAllLocationsForUser(User user) {
        List<Location> ownedLocations = locationRepository.findByOwner(user);
        List<Location> sharedLocations = locationSharingRepository.findByUser(user)
            .stream()
            .map(LocationSharing::getLocation)
            .toList();
        ownedLocations.addAll(sharedLocations);
        return ownedLocations;
    }

    public void manageAccess(Location location, User user, AccessLevel accessLevel) {
        LocationSharing locationSharing = locationSharingRepository.findByLocation(location)
            .stream()
            .filter(ls -> ls.getUser().equals(user))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("User does not have access to this location"));
        locationSharing.setAccessLevel(accessLevel);
        locationSharingRepository.save(locationSharing);
    }
}
