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
public class Category {
    private int id;
    private String name;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}