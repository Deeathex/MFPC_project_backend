package com.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllModelListsDTO {
    private List<MovieDTO> movies = new ArrayList<>();
    private List<DirectorDTO> directors = new ArrayList<>();
    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<UserDTO> users = new ArrayList<>();
}
