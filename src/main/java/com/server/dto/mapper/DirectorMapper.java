package com.server.dto.mapper;

import com.server.dto.business.DirectorDTO;
import com.server.model.db1.Director;

import java.util.ArrayList;
import java.util.List;

public class DirectorMapper {
    public static Director directorDTOTODirector(DirectorDTO directorDTO) {
        Director director = new Director();

        director.setId(directorDTO.getId());
        director.setName(directorDTO.getName());
        director.setNationality(directorDTO.getNationality());

        return director;
    }

    public static DirectorDTO directorToDirectorDTO(Director director) {
        DirectorDTO directorDTO = new DirectorDTO();

        directorDTO.setId(director.getId());
        directorDTO.setName(director.getName());
        directorDTO.setNationality(director.getNationality());

        return directorDTO;
    }

    public static List<DirectorDTO> directorsToDirectorsDTO(List<Director> directors) {
        List<DirectorDTO> directorsDTO = new ArrayList<>();

        for (Director director : directors) {
            directorsDTO.add(directorToDirectorDTO(director));
        }

        return directorsDTO;
    }
}
