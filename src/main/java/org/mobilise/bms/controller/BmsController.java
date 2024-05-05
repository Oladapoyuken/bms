package org.mobilise.bms.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mobilise.bms.dto.ApiResponseDto;
import org.mobilise.bms.dto.BookDto;
import org.mobilise.bms.dto.BookList;
import org.mobilise.bms.dto.UpdateBookDto;
import org.mobilise.bms.service.BmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by: @oladapoyuken
 * Date: 04/05/2024
 */

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/book")
public class BmsController {

    private final BmsService bmsService;

    @Operation(summary = "Create new Book")
    @PostMapping
    public ResponseEntity<ApiResponseDto<BookDto>> createBook(@RequestBody @Valid BookDto bookDto) {
        log.info("createBook: {}", bookDto);
        ApiResponseDto<BookDto> response = bmsService.createBook(bookDto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Fetch book details using the book id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BookDto>> getBookById(@PathVariable("id") Long id){
        ApiResponseDto<BookDto> response = bmsService.getBook(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Delete book using the book id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> romoveBookById(@PathVariable("id") Long id){
        ApiResponseDto response = bmsService.deleteBook(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Update book details")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<BookDto>> updateBook(
            @RequestBody @Valid UpdateBookDto bookDto,
            @PathVariable("id") Long id
    ) {
        log.info("updateBook: {}", bookDto);
        ApiResponseDto<BookDto> response = bmsService.updateBook(bookDto, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Fetch all books")
    @GetMapping
    public ResponseEntity<ApiResponseDto<BookList>> fetchBooks(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        ApiResponseDto<BookList> response = bmsService.getListOfBooks(title, author, pageSize, pageNumber);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
