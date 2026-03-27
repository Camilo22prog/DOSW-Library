package edu.eci.dosw.DOSW_Library.core.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private String id;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public boolean isAvailable() {
        return availableCopies > 0;
    }
}