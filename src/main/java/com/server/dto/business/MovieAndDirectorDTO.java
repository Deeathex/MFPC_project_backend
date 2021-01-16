package com.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieAndDirectorDTO {
    private MovieDTO movie;
    private DirectorDTO director;
}
