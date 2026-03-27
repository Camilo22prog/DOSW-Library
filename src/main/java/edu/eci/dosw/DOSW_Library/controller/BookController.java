package edu.eci.dosw.DOSW_Library.controller;

import edu.eci.dosw.DOSW_Library.controller.dto.BookDTO;
import edu.eci.dosw.DOSW_Library.controller.mapper.BookMapper;
import edu.eci.dosw.DOSW_Library.core.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO dto) {
        var book = BookMapper.toEntity(dto);
        var saved = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(BookMapper.toDTO(saved));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> list = bookService.getAllBooks().stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN')")
    public ResponseEntity<BookDTO> getBookById(@PathVariable String id) {
        return ResponseEntity.ok(BookMapper.toDTO(bookService.getBookById(id)));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookDTO> updateStock(
            @PathVariable String id,
            @RequestParam int totalCopies,
            @RequestParam int availableCopies) {
        var book = bookService.updateStock(id, totalCopies, availableCopies);
        return ResponseEntity.ok(BookMapper.toDTO(book));
    }
}
