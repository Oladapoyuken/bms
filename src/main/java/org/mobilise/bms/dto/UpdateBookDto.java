package org.mobilise.bms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by: @oladapoyuken
 * Date: 04/05/2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookDto {

    @NotBlank(message = "title is required")
    private String title;

    @NotBlank(message = "author is required")
    private String author;

    private String publisher;
    private String description;
    private BigDecimal price;
    private int pages;

    @Min(value = 1, message = "publicationYear is required")
    private long publicationYear;
}
