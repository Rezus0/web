package com.example.web_2.user.profilePicture;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureService {
    String getProfilePicture(String username);
    String changeProfilePicture(MultipartFile photo, String username);
    void removeProfilePicture();
}
