package com.example.locationsystem.repository;

import com.example.locationsystem.model.Location;
import com.example.locationsystem.model.LocationSharing;
import com.example.locationsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationSharingRepository extends JpaRepository<LocationSharing, Long> {
    List<LocationSharing> findByLocation(Location location);
    List<LocationSharing> findByUser(User user);
    boolean existsByLocationAndUser(Location location, User user);
}
