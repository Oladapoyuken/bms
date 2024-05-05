package org.mobilise.bms.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by: @oladapoyuken
 * Date: 04/05/2024
 */

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "publication year")
    private Long publicationYear;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "pages")
    private int pages;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

}
