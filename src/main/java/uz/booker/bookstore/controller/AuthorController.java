package uz.booker.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.booker.bookstore.dto.AuthorDto;
import uz.booker.bookstore.service.interfaces.AuthorService;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Author", description = "Author management APIs")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Operation(summary = "get-author", description = "bar")
    @GetMapping
    public ResponseEntity<Page<AuthorDto>> getAllAuthor(Pageable pageable){
        Page<AuthorDto> book = authorService.getAllAuthor(pageable);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "add-author", description = "bar")
    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@RequestBody AuthorDto authorDto){
        authorService.addAuthor(authorDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "update-author", description = "bar")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id, @RequestBody AuthorDto authorDto){
        authorService.updateAuthor(id,authorDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "delete-author", description = "bar")
    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId){
        authorService.deleteAuthor(authorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}