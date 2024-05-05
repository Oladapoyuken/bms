package org.mobilise.bms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mobilise.bms.domain.Book;
import org.mobilise.bms.dto.ApiResponseDto;
import org.mobilise.bms.dto.BookDto;
import org.mobilise.bms.dto.BookList;
import org.mobilise.bms.dto.UpdateBookDto;
import org.mobilise.bms.repository.BookRepository;
import org.mobilise.bms.util.BmsUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BmsServiceTest {


    @InjectMocks
    BmsServiceImpl bmsService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BmsUtils bmsUtils;

    BookDto bookDto;
    Book book;
    UpdateBookDto updateBookDto;
    Book upDatedBook;

    @BeforeEach
    void setUp(){
        String isbn1 = "ABC-123";
        when(bmsUtils.generateIsbn()).thenReturn(isbn1);

        bookDto = BookDto.builder()
                .price(BigDecimal.TEN)
                .title("First Stage")
                .publicationYear(2023)
                .author("Yusuf")
                .description("Coding Assessment")
                .pages(10)
                .publisher("Coderbyte")
                .build();

        book = Book.builder()
                .id(1L)
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

        updateBookDto = UpdateBookDto.builder()
                .price(BigDecimal.TWO)
                .title("Second Stage")
                .publicationYear(2024)
                .author("CTO")
                .description("Meet the team")
                .pages(20)
                .publisher("Mobilise")
                .build();

        upDatedBook = Book.builder()
                .id(1L)
                .pages(updateBookDto.getPages())
                .price(updateBookDto.getPrice())
                .title(updateBookDto.getTitle())
                .isbn(bmsUtils.generateIsbn())
                .author(updateBookDto.getAuthor())
                .createdDate(new Date())
                .description(updateBookDto.getDescription())
                .publisher(updateBookDto.getPublisher())
                .publicationYear(updateBookDto.getPublicationYear())
                .build();
    }

    @Test
    void createBook() {
        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(book);

        BookDto dto1 = BookDto.builder()
                .id(1L)
                .price(BigDecimal.TEN)
                .title("First Stage")
                .publicationYear(2023)
                .author("Yusuf")
                .description("Coding Assessment")
                .pages(10)
                .publisher("Coderbyte")
                .isbn("ABC-123")
                .build();

        ApiResponseDto actualResponse = bmsService.createBook(bookDto);

        assertEquals(ApiResponseDto.builder()
                .code(201)
                .message("Book created successfully")
                .data(dto1).build(), actualResponse
        );

    }

    @Test
    void updateBooThatExist() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        BookDto dto1 = BookDto.builder()
                .id(1L)
                .price(BigDecimal.TWO)
                .title("Second Stage")
                .publicationYear(2024)
                .author("CTO")
                .description("Meet the team")
                .pages(20)
                .publisher("Mobilise")
                .isbn("ABC-123")
                .build();

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(upDatedBook);

        ApiResponseDto actualResponse = bmsService.updateBook(updateBookDto, 1L);

        assertEquals(ApiResponseDto.builder()
                .code(200)
                .message("Book updated successfully")
                .data(dto1).build(), actualResponse
        );
    }

    @Test
    void updateBooThatDoesNoExist() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        ApiResponseDto actualResponse = bmsService.updateBook(updateBookDto, 1L);
        assertEquals(ApiResponseDto.builder()
                        .code(404)
                        .message("Cannot find book with id " + 1L).build(),
                actualResponse
        );
    }

    @Test
    void getBookWithCorrectId() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        BookDto dto1 = BookDto.builder()
                .id(1L)
                .price(BigDecimal.TEN)
                .title("First Stage")
                .publicationYear(2023)
                .author("Yusuf")
                .description("Coding Assessment")
                .pages(10)
                .publisher("Coderbyte")
                .isbn("ABC-123")
                .build();
        ApiResponseDto actualResponse = bmsService.getBook(1L);
        assertEquals(ApiResponseDto.builder()
                        .code(200)
                        .data(dto1).build(),
                actualResponse
        );
    }

    @Test
    void getBookWithWrongId() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        ApiResponseDto actualResponse = bmsService.getBook(1L);
        assertEquals(ApiResponseDto.builder()
                        .code(404)
                        .message("Cannot find book with id " + 1L).build(),
                actualResponse
        );
    }


    @Test
    void getListOfBooksWithFilterParameter() {
        int pageNumber = 1;
        int pageSize = 10;
        Page page = new Page() {
            @Override
            public int getTotalPages() {
                return 1;
            }

            @Override
            public long getTotalElements() {
                return 1;
            }

            @Override
            public Page map(Function converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 1;
            }

            @Override
            public List getContent() {
                return List.of(book);
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return null;
            }
        };
        BookDto dto1 = BookDto.builder()
                .id(1L)
                .price(BigDecimal.TEN)
                .title("First Stage")
                .publicationYear(2023)
                .author("Yusuf")
                .description("Coding Assessment")
                .pages(10)
                .publisher("Coderbyte")
                .isbn("ABC-123")
                .build();

        BookList bookList = new BookList();
        bookList.setBooks(List.of(dto1));
        bookList.setPageNumber(1);
        bookList.setPageSize(10);
        bookList.setTotalPages(1);
        bookList.setTotalCount(1);
        ApiResponseDto expectedResponse = ApiResponseDto.builder()
                .code(200)
                .data(bookList)
                .build();
        Mockito.when(bookRepository.findAll((Specification<Book>) any(), (Pageable) any())).thenReturn(page);
        ApiResponseDto actualResponse = bmsService.getListOfBooks("stage", "", pageSize, pageNumber);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getListOfBooksWithoutFilterParamter() {
        int pageNumber = 1;
        int pageSize = 10;
        Page page = new Page() {

            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public Page map(Function converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List getContent() {
                return List.of();
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return null;
            }
        };

        BookList bookList = new BookList();
        bookList.setBooks(Collections.emptyList());
        bookList.setPageNumber(pageNumber);
        bookList.setPageSize(pageSize);
        bookList.setTotalPages(0);
        bookList.setTotalCount(0);

        ApiResponseDto expectedResponse = ApiResponseDto.builder()
                .code(200)
                .data(bookList)
                .build();
        Mockito.when(bookRepository.findAll((Specification<Book>) any(), (Pageable) any())).thenReturn(page);
        ApiResponseDto actualResponse = bmsService.getListOfBooks("mobilise", "", pageSize, pageNumber);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void deleteExistingBook() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        ApiResponseDto actualResponse = bmsService.deleteBook(1L);
        assertEquals(ApiResponseDto.builder()
                .code(200)
                .message("Book deleted successfully").build(), actualResponse
        );
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNonExistingBook() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        ApiResponseDto actualResponse = bmsService.getBook(1L);
        assertEquals(ApiResponseDto.builder()
                .code(404)
                .message("Cannot find book with id " + 1L).build(), actualResponse
        );
    }
}