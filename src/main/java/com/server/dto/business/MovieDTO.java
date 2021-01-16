package com.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private String id;
    private String title;
    private Collection<DirectorDTO> directors = new ArrayList<>();
}
