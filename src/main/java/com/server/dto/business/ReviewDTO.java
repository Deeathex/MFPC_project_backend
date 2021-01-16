package com.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private String id;
    private String comment;
    private Date date;
    private int rating;
    private UserDTO user;
}
