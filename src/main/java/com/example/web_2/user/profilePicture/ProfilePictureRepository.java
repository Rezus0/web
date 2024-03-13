package com.example.web_2.user.profilePicture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, UUID> {
    Optional<ProfilePicture> findProfilePictureByUserUsername(String username);

}
