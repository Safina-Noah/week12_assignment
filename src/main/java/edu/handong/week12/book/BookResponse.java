package edu.handong.week12.book;

public record BookResponse(
        Long id, String title, String author, Integer price) {
    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getPrice());
    }
}