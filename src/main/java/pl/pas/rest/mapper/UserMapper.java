package pl.pas.rest.mapper;

import pl.pas.rest.dto.UserGetDTO;
import pl.pas.rest.dto.UserPostDTO;
import pl.pas.rest.exceptions.UserNotFoundException;
import pl.pas.rest.model.Administrator;
import pl.pas.rest.model.Client;
import pl.pas.rest.model.ResourceManager;
import pl.pas.rest.model.User;

public class UserMapper {
    public UserGetDTO convertToUserGetDTO(User user) {
        return new UserGetDTO(user.getUuid(), user.getIsActive(), user.getLogin(), user.getRole(), user.getName(), user.getSurname());
    }

    public User convertToUser(UserPostDTO userPostDTO) throws UserNotFoundException {
        switch (userPostDTO.getRole()) {
            case "CLIENT":
                return new Client(userPostDTO.getIsActive(), userPostDTO.getLogin(), userPostDTO.getPassword(), userPostDTO.getRole(), userPostDTO.getName(), userPostDTO.getSurname());
            case "MANAGER":
                return new ResourceManager(userPostDTO.getIsActive(), userPostDTO.getLogin(), userPostDTO.getPassword(), userPostDTO.getRole(), userPostDTO.getName(), userPostDTO.getSurname());
            case "ADMIN":
                return new Administrator(userPostDTO.getIsActive(), userPostDTO.getLogin(), userPostDTO.getPassword(), userPostDTO.getRole(), userPostDTO.getName(), userPostDTO.getSurname());
            default:
                throw new UserNotFoundException("Bad role was given.");
        }
    }
}
