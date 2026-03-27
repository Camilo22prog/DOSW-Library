package edu.eci.dosw.DOSW_Library.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private String id;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
}