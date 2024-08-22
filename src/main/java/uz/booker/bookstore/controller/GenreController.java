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
import uz.booker.bookstore.dto.GenreDto;
import uz.booker.bookstore.service.interfaces.GenreService;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Genre", description = "Genre management APIs")
public class GenreController {

    @Autowired
    GenreService genreService;

    @Operation(summary = "get-all-genre", description = "bar")
    @GetMapping
    public ResponseEntity<Page<GenreDto>> getAllGenre(Pageable pageable){
        Page<GenreDto> book = genreService.getAllGenre(pageable);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "add-genre", description = "bar")
    @PostMapping
    public ResponseEntity<GenreDto> addGenre(@RequestBody GenreDto genreDto){
        genreService.addGenre(genreDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "update-genre", description = "bar")
    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(@PathVariable Long id, @RequestBody GenreDto genreDto){
        genreService.updateGenre(id,genreDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "delete-genre", description = "bar")
    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long genreId){
        genreService.deleteGenre(genreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
