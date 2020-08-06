package com.nelson.libraryapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {

    private long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String genre;

    @NotEmpty
    private String code;

}
