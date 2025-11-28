package com.books.dto;

import java.util.UUID;

public class LibraryDTO {
    private UUID id;
    private String name;
    private String address;
    private String workingHours;

    public LibraryDTO() {}

    public LibraryDTO(UUID id, String name, String address, String workingHours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.workingHours = workingHours;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
}