package com.server.dto.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAndUserDTO {
    private ReviewDTO review;
    private UserDTO user;
}
