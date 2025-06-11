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
public class BorrowRecord {
    private int id;
    private int userId;
    private int bookId;
    private Timestamp borrowDate;
    private Timestamp returnDate;
    private boolean returned;
}