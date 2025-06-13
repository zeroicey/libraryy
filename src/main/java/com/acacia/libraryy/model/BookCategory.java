package com.acacia.libraryy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCategory {
    private int bookId;
    private int categoryId;
    private Timestamp createdAt;
}