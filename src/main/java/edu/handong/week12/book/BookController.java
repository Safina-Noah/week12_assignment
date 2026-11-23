package edu.handong.week12.book;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Book", description = "책 관리 API")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @Operation(summary = "책 등록", description = "제목, 저자, 가격을 받아 새 책을 등록합니다.")
    @PostMapping
    // POST 요청에 대해 BookRequest 객체로 service에 전달한다. 이때 객체 내에 정의된 validation 조건을 검사한다.
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        //저장 이후 Service에서 반환된 BookResponse 객체를 받아온다.
        BookResponse created = service.create(request);
        // 클라이언트에 ResponseEntity를 전달하여 성공 시 201 응답 코드를 전달한다.
        return ResponseEntity.created(URI.create("/api/books/" + created.id())).body(created);
    }

    @Operation(summary = "책 목록 조회", description = "페이지 번호, 크기, 정렬 조건으로 책을 조회합니다.")
    @GetMapping
    public Page<BookResponse> findAll(@ParameterObject Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
