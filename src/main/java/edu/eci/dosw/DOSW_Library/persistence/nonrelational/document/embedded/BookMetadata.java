package edu.eci.dosw.DOSW_Library.persistence.nonrelational.document.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookMetadata {
    private Integer pages;
    private String language;
    private String publisher;
}