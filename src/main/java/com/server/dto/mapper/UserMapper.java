package com.server.dto.mapper;

import com.server.dto.business.UserDTO;
import com.server.model.db2.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User userDTOTOUser(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        return user;
    }

    public static UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    public static List<UserDTO> usersToUsersDTO(List<User> users) {
        List<UserDTO> usersDTO = new ArrayList<>();

        for (User user : users) {
            usersDTO.add(userToUserDTO(user));
        }

        return usersDTO;
    }
}
