package com.example.locationsystem.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LocationSharing> sharedWith = new HashSet<>();

    // Constructors, getters, and setters
    public Location() {}

    public Location(String name, String address, User owner) {
        this.name = name;
        this.address = address;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<LocationSharing> getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(Set<LocationSharing> sharedWith) {
        this.sharedWith = sharedWith;
    }

    public void addSharing(LocationSharing sharing) {
        sharedWith.add(sharing);
        sharing.setLocation(this);
    }

    public void removeSharing(LocationSharing sharing) {
        sharedWith.remove(sharing);
        sharing.setLocation(null);
    }
}
