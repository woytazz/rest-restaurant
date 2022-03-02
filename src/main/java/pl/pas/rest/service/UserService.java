package pl.pas.rest.service;

import pl.pas.rest.dto.PasswordDTO;
import pl.pas.rest.dto.UserGetDTO;
import pl.pas.rest.dto.UserPostDTO;
import pl.pas.rest.exceptions.UserLoginTakenException;
import pl.pas.rest.exceptions.UserNotFoundException;
import pl.pas.rest.mapper.UserMapper;
import pl.pas.rest.model.User;
import pl.pas.rest.repository.UserRepository;
import pl.pas.rest.security.JWSGeneratorVerifier;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.*;

public class UserService {
    @Inject
    private UserRepository repository;

    @Inject
    private UserMapper mapper;

    public UserGetDTO createUser(UserPostDTO userPostDTO) throws UserLoginTakenException, UserNotFoundException {
        if (repository.add(mapper.convertToUser(userPostDTO))) {
           return findUserByLogin(userPostDTO.getLogin());
        } else {
            throw new UserLoginTakenException("Login is taken");

        }
    }

    public UserGetDTO readUser(UUID uuid) throws UserNotFoundException {
        Optional<User> optionalUser = repository.get(uuid);

        if (optionalUser.isPresent()) {
            return mapper.convertToUserGetDTO(optionalUser.get());
        } else {
            throw new UserNotFoundException("Bad user UUID");
        }
    }

    public List<UserGetDTO> readAllUsers() {
        List<UserGetDTO> userGetDTOList = new ArrayList<>();

        for (User user : repository.getList()) {
            userGetDTOList.add(mapper.convertToUserGetDTO(user));
        }
        return Collections.unmodifiableList(userGetDTOList);
    }

    public UserGetDTO updateUser(UUID uuid, UserPostDTO userPostDTO) throws UserNotFoundException {
        if (repository.update(uuid, mapper.convertToUser(userPostDTO))) {
            return readUser(uuid);
        } else {
            throw new UserNotFoundException("Bad user UUID");
        }
    }

    public UserGetDTO findUserByLogin(String login) throws UserNotFoundException {
        Optional<User> optionalUser = repository.findByLogin(login);

        if (optionalUser.isPresent()) {
            return mapper.convertToUserGetDTO(optionalUser.get());
        } else {
            throw new UserNotFoundException("Bad user login");
        }
    }

    public List<UserGetDTO> matchUsersByLogin(String login) {
        List<UserGetDTO> userGetDTOList = new ArrayList<>();

        for (User user : repository.getList()) {
            if (user.getLogin().contains(login)) {
                userGetDTOList.add(mapper.convertToUserGetDTO(user));
            }
        }
       return Collections.unmodifiableList(userGetDTOList);
    }

    public void changeUserActivity(UUID uuid) throws UserNotFoundException {
        Optional<User> optionalUser = repository.get(uuid);

        if (optionalUser.isPresent()) {
            optionalUser.get().setIsActive(!optionalUser.get().getIsActive());
        } else {
            throw new UserNotFoundException("Bad user UUID");
        }
    }

    public void updatePassword(UUID uuid, PasswordDTO passwordDTO) throws UserNotFoundException {
        Optional<User> optionalUser = repository.get(uuid);
        if (optionalUser.isPresent()) {
            if (passwordDTO.getPassword().equals(passwordDTO.getRetryPassword())) {
                repository.changePassword(optionalUser.get(), passwordDTO.getPassword());
            } else {
                throw new BadRequestException("Passwords not match");
            }
        } else {
            throw new UserNotFoundException("Bad user UUID");
        }
    }

    public boolean validateUpdateJSON(String sign, UUID uuid, UserPostDTO userPostDTO) {
        UUID checkUUID = UUID.fromString((String) JWSGeneratorVerifier.decodeJWS(sign).get("uuid"));
        Boolean isActive = (Boolean) JWSGeneratorVerifier.decodeJWS(sign).get("isActive");
        String login = (String) JWSGeneratorVerifier.decodeJWS(sign).get("login");
        String role = (String) JWSGeneratorVerifier.decodeJWS(sign).get("role");

        if (!checkUUID.equals(uuid)) {
            return false;
        } else if (!isActive.equals(userPostDTO.getIsActive())) {
            return false;
        } else if (!login.equals(userPostDTO.getLogin())) {
            return false;
        } else return role.equals(userPostDTO.getRole());
    }

    public boolean validateUUID(String sign, UUID uuid) {
        return uuid.equals(UUID.fromString((String) JWSGeneratorVerifier.decodeJWS(sign).get("uuid")));
    }
}
