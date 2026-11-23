package edu.handong.week12.book;

import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


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

  public Page<BookResponse> findAll(Pageable pageable) {
    // DB에서 해당 페이지를 조회하고 페이지 정보를 유지한 채 DTO로 변환한다.
    return repository.findAll(pageable).map(BookResponse::from);
  }

  public BookResponse findById(Long id) {
    return BookResponse.from(findEntity(id));
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
