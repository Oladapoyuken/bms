package org.mobilise.bms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mobilise.bms.dto.ApiResponseDto;
import org.mobilise.bms.dto.BookDto;
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

    //create new book
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody @Valid BookDto bookDto) {
        log.info("createBook: {}", bookDto);
        ApiResponseDto response = bmsService.createBook(bookDto);
        return getResponse(response);
    }

    //fetch book details using the book id
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable("id") Long id){
        ApiResponseDto response = bmsService.getBook(id);
        return getResponse(response);
    }

    //delete book using the book id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> romoveBookById(@PathVariable("id") Long id){
        ApiResponseDto response = bmsService.deleteBook(id);
        return getResponse(response);
    }

    //update book by passing the book id as a path variable and updated details in the request body
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @RequestBody @Valid UpdateBookDto bookDto,
            @PathVariable("id") Long id
    ) {
        log.info("updateBook: {}", bookDto);
        ApiResponseDto response = bmsService.updateBook(bookDto, id);
        return getResponse(response);
    }

    //filter books using any key word in book title or author
    @GetMapping("/search")
    public ResponseEntity<?> searchBook(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        ApiResponseDto response = bmsService.getListOfBooks(title, author, pageSize, pageNumber);
        return getResponse(response);
    }

    //map response with the right http status code
    private ResponseEntity<ApiResponseDto> getResponse(ApiResponseDto apiResponseDto) {
        return ResponseEntity.status(apiResponseDto.getCode()).body(apiResponseDto);
    }
}
