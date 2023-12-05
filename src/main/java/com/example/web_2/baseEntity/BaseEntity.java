package com.example.web_2.baseEntity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {
    private UUID id;
    private LocalDateTime created;
    private LocalDateTime modified;

    @Id
    @GeneratedValue(generator = "uuid2")
    public UUID getId() {
        return id;
    }

    @NotNull(message = "Created time can't be null")
    public LocalDateTime getCreated() {
        return created;
    }

    @NotNull(message = "Modified time can't be null")
    public LocalDateTime getModified() {
        return modified;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
}
