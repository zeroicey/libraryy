package com.acacia.libraryy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private int id;
    private String title;
    private String author;
    private String tag;
    private String status; // ENUM('available', 'borrowed')
    private String description;
    private String coverImage;
    private String isbn;
    private int sumNumber;
    private int restNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}