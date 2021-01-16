package com.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorsAndUsersDTO {
    private List<DirectorDTO> directors;
    private List<UserDTO> users;
}
