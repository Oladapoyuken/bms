package org.mobilise.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by: @oladapoyuken
 * Date: 04/05/2024
 */

@Data
public class BookList {
    private List<BookDto> books;
    private int pageNumber;
    private int pageSize;
    private long totalCount;
    private int totalPages;
}
