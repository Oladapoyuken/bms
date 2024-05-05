package org.mobilise.bms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mobilise.bms.dto.ApiResponseDto;
import org.mobilise.bms.dto.BookDto;
import org.mobilise.bms.dto.UpdateBookDto;
import org.springframework.stereotype.Service;

/**
 * Created by: @oladapoyuken
 * Date: 04/05/2024
 */


public interface BmsService {
    ApiResponseDto createBook(BookDto bookDto);
    ApiResponseDto updateBook(UpdateBookDto bookDto, Long id);
    ApiResponseDto deleteBook(Long id);
    ApiResponseDto getBook(Long id);
    ApiResponseDto getListOfBooks(int pageSize, int pageNumber);
    ApiResponseDto getListOfBooksWithFilter(String title, String author, int pageSize, int pageNumber);
}
