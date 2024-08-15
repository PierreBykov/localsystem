package com.example.locationsystem.repository;

import com.example.locationsystem.model.Location;
import com.example.locationsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByOwner(User owner);
}
