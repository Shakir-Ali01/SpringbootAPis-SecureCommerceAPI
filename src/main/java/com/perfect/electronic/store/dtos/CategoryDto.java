package com.perfect.electronic.store.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private String categoryId;
    @NotBlank
    @Size(min = 4, message = "Title must be minimum of 4 character")
    private String title;
    @NotBlank(message = "Description is Required !!")
    private String description;
//    @NotBlank(message = "Cover Image Required !!")
    private String coverImage;
}
