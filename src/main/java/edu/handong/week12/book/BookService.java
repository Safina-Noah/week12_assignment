package edu.handong.week12.book;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BookService {
  private final BookRepository repository;

  public BookService(BookRepository repository) {
    this.repository = repository;
  }
  public BookResponse create(BookRequest request) {
    Book saved = repository.save(new Book(request)); // INSERT
    return BookResponse.from(saved);
  }

  // Pageable에는 요청 페이지 번호, 페이지 크기, 정렬 조건이 담긴다.
  public Page<BookResponse> findAll(Pageable pageable) {
    // Repository가 DB에서 해당 페이지를 조회한다.
    Page<Book> books = repository.findAll(pageable);
    // 각 Book을 응답 DTO로 변환한다.
    return books.map(BookResponse::from);
  }

  public BookResponse findById(Long id) {
    // ID로 조회한다. 조회한 Book이 없을 수 있기에 Optional을 사용한다.
    Optional<Book> optionalBook = repository.findById(id);
    // 값이 있으면 Book을 꺼낸다. 없으면 404 상태 정보를 가진 예외를 발생시킨다.
    // 이 예외는 GlobalExceptionHandler가 받아 공통 JSON 오류 응답으로 변환한다.
    Book book = optionalBook.orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Book not found: " + id));
    // 조회된 엔티티를 클라이언트에게 반환할 응답 DTO로 변환한다.
    return BookResponse.from(book);
  }

  public BookResponse update(Long id, BookRequest request) {
    Book book = findEntity(id);
    book.update(request);
    return BookResponse.from(repository.save(book));
  }

  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found: " + id);
    }
    repository.deleteById(id);
  }

  private Book findEntity(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Book not found: " + id));
  }
}
