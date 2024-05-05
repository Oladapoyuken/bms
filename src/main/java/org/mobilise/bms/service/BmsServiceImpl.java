package org.mobilise.bms.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mobilise.bms.domain.Book;
import org.mobilise.bms.dto.*;
import org.mobilise.bms.repository.BookRepository;
import org.mobilise.bms.util.BmsUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: @oladapoyuken
 * Date: 05/05/2024
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class BmsServiceImpl implements BmsService {

    private final BookRepository bookRepository;
    private final BmsUtils bmsUtils;

    @Override
    public ApiResponseDto createBook(BookDto bookDto) {

        //build a book entity from dto
        Book book = Book.builder()
                .pages(bookDto.getPages())
                .price(bookDto.getPrice())
                .title(bookDto.getTitle())
                .isbn(bmsUtils.generateIsbn())
                .author(bookDto.getAuthor())
                .createdDate(new Date())
                .description(bookDto.getDescription())
                .publisher(bookDto.getPublisher())
                .publicationYear(bookDto.getPublicationYear())
                .build();

        //save new book with a unique ISBN
        book = bookRepository.save(book);

        //map new book entity to dto
        bookDto = toBookDto(book);
        return ApiResponseDto.builder()
                .code(201)
                .message("Book created successfully")
                .data(bookDto)
                .build();
    }

    @Override
    public ApiResponseDto updateBook(UpdateBookDto bookDto, Long id) {

        //check if book to be updated already exist using the book id
        Optional<Book> bookExisted = bookRepository.findById(id);
        if(bookExisted.isEmpty()) {
            return ApiResponseDto.builder()
                    .code(404)
                    .message("Cannot find book with id " + id)
                    .build();
        }

        //map the new details to the book entity if present
        Book book = toUpdateBook(bookExisted.get(), bookDto);

        //save updated book entity
        book = bookRepository.save(book);

        //map new book entity to dto
        BookDto finalBook = toBookDto(book);
        return ApiResponseDto.builder()
                .code(200)
                .message("Book updated successfully")
                .data(finalBook)
                .build();
    }

    @Override
    public ApiResponseDto deleteBook(Long id) {

        //check if book to be deleted exists using the book id
        Optional<Book> bookExisted = bookRepository.findById(id);
        if(bookExisted.isEmpty()) {
            return ApiResponseDto.builder()
                    .code(404)
                    .message("Cannot find book with id " + id)
                    .build();
        }

        //delete book if exists
        bookRepository.deleteById(id);
        return ApiResponseDto.builder()
                .code(200)
                .message("Book deleted successfully")
                .build();
    }

    @Override
    public ApiResponseDto getBook(Long id) {

        //check if book exist
        Optional<Book> bookExisted = bookRepository.findById(id);
        if(bookExisted.isEmpty()) {
            return ApiResponseDto.builder()
                    .code(404)
                    .message("Cannot find book with id " + id)
                    .build();
        }

        //map book entity to dto to get details
        BookDto bookDto = toBookDto(bookExisted.get());
        return ApiResponseDto.builder()
                .code(200)
                .data(bookDto)
                .build();
    }

    @Override
    public ApiResponseDto getListOfBooks(String title, String author, int pageSize, int pageNumber) {

        //initialize pagination request in descending order by id
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by("id").descending());

        //use jpa specification executor for flexible query using jpa
        Page<Book> bookPage = bookRepository.findAll(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(title)) {
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
                }
                if (StringUtils.isNotBlank(author)) {
                    predicates.add(criteriaBuilder.like(root.get("author"), "%" + author + "%"));
                }
                if (!predicates.isEmpty()) {
                    Predicate[] predicateArr = new Predicate[predicates.size()];
                    return criteriaBuilder.and(predicates.toArray(predicateArr));
                }
                else {
                    return null;
                }
            }
        }, pageable);

        //extract book entity list to dto list
        List<BookDto> books = toBookDtoList(bookPage.getContent());

        BookList bookList = new BookList();
        bookList.setBooks(books);
        bookList.setPageNumber(pageNumber);
        bookList.setPageSize(pageSize);
        bookList.setTotalPages(bookPage.getTotalPages());
        bookList.setTotalCount(bookPage.getTotalElements());

        return ApiResponseDto.builder()
                .code(200)
                .data(bookList)
                .build();
    }

    //map book entity to dto
    private BookDto toBookDto(Book book) {
        return BookDto.builder()
                .price(book.getPrice())
                .title(book.getTitle())
                .author(book.getAuthor())
                .id(book.getId())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .pages(book.getPages())
                .publisher(book.getPublisher())
                .publicationYear(book.getPublicationYear())
                .build();
    }

    //update book entity with values from book dto
    private Book toUpdateBook(Book book, UpdateBookDto bookDto) {
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublisher(bookDto.getPublisher());
        book.setDescription(bookDto.getDescription());
        book.setPages(bookDto.getPages());
        book.setPrice(bookDto.getPrice());
        book.setPublicationYear(bookDto.getPublicationYear());
        return book;
    }

    //extract list of book entities into a list of book dtos
    private List<BookDto> toBookDtoList(List<Book> books) {
        return books.stream()
                .map(this::toBookDto)
                .collect(Collectors.toList());
    }
}
