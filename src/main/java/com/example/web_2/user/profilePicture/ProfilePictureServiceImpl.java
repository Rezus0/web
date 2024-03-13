package com.example.web_2.user.profilePicture;

import com.example.web_2.user.User;
import com.example.web_2.user.UserRepository;
import com.example.web_2.user.exception.UserNotFoundException;
import com.example.web_2.util.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {
    private ProfilePictureRepository profilePictureRepository;
    private UserRepository userRepository;
    private S3Service s3Service;

    @Override
    public String getProfilePicture(String username) {
        Optional<ProfilePicture> optionalProfilePicture = profilePictureRepository
                .findProfilePictureByUserUsername(username);
        if (optionalProfilePicture.isEmpty())
            return "https://i.imgur.com/hepj9ZS.png";
        else return optionalProfilePicture.get().getLink();
    }

    @Override
    public String changeProfilePicture(MultipartFile picture, String username) {
        Optional<ProfilePicture> optionalProfilePicture = profilePictureRepository
                .findProfilePictureByUserUsername(username);
        Optional<User> optionalUser = userRepository
                .findUserByUsername(username);
        if (optionalUser.isEmpty())
            throw new UserNotFoundException(String.format("User with username \"%s\" not found", username));
        User user = optionalUser.get();
        String pictureLink = s3Service.load(picture, user.getId().toString());
        ProfilePicture newPicture;
        if (optionalProfilePicture.isEmpty()) {
            newPicture = new ProfilePicture(pictureLink, user);
            newPicture.setCreated(LocalDateTime.now());
            newPicture.setModified(LocalDateTime.now());
        } else {
            newPicture = optionalProfilePicture.get();
            newPicture.setLink(pictureLink);
            newPicture.setModified(LocalDateTime.now());
        }
        profilePictureRepository.saveAndFlush(newPicture);
        return newPicture.getLink();
    }

    @Override
    public void removeProfilePicture() {

    }

    @Autowired
    public void setProfilePictureRepository(ProfilePictureRepository profilePictureRepository) {
        this.profilePictureRepository = profilePictureRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setS3Service(S3Service s3Service) {
        this.s3Service = s3Service;
    }
}
