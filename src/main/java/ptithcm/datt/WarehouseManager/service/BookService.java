package ptithcm.datt.WarehouseManager.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.exception.BookException;
import ptithcm.datt.WarehouseManager.model.Author;
import ptithcm.datt.WarehouseManager.model.Book;
import ptithcm.datt.WarehouseManager.model.Category;
import ptithcm.datt.WarehouseManager.model.Publisher;
import ptithcm.datt.WarehouseManager.repository.AuthorRepository;
import ptithcm.datt.WarehouseManager.repository.BookRepository;
import ptithcm.datt.WarehouseManager.repository.CategoryRepository;
import ptithcm.datt.WarehouseManager.repository.PublisherRepository;
import ptithcm.datt.WarehouseManager.request.BookExcelRequest;
import ptithcm.datt.WarehouseManager.request.BookRequest;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    public Book findBookById(Long bookId) throws BookException {
        Optional<Book> opt = bookRepository.findById(bookId);
        if(opt.isPresent()) {
            return opt.get();
        }throw new BookException("Book not found with id-"+bookId);
    }

    @Transactional
    public List<Book> createBooks(List<BookExcelRequest> bookRequests) {
        List<Book> books = new ArrayList<>();

        for (BookExcelRequest bookRequest : bookRequests) {
            Publisher publisher = publisherRepository.findByPublisherName(bookRequest.getPublisherName())
                    .orElseThrow(() -> new RuntimeException("Publisher not found: " + bookRequest.getPublisherName()));

            Category category = categoryRepository.findByCategoryName(bookRequest.getCategoryName())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + bookRequest.getCategoryName()));

            Book book = new Book();
            book.setBookName(bookRequest.getBookName());
            book.setTitle(bookRequest.getTitle());
            book.setEdition(bookRequest.getEdition());
            book.setLanguage(bookRequest.getLanguage());
            book.setPublicationYear(bookRequest.getPublicationYear());
            book.setNumberOfPage(bookRequest.getNumberOfPage());
            book.setCategory(category);
            book.setPublisher(publisher);
            book.setQuantity(bookRequest.getQuantity());
            book.setPrice(bookRequest.getPrice());
            book.setCreateAt(LocalDateTime.now());
            book.setStatus("ACTIVE");
            book.setImage(bookRequest.getImage());

            Set<Author> authors = new HashSet<>();
            for (String authorName : bookRequest.getAuthorName()) {
                Author author = authorRepository.findByAuthorName(authorName)
                        .orElseThrow(() -> new RuntimeException("Author not found: " + authorName));
                authors.add(author);
            }
            book.setAuthors(authors);

            books.add(bookRepository.save(book));
        }

        return books;
    }


    @Transactional
    public Book createBook(BookRequest bookRequest) {
        Publisher publisher = publisherRepository.findById(bookRequest.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Book book = new Book();
        book.setBookName(bookRequest.getBookName());
        book.setTitle(bookRequest.getTitle());
        book.setEdition(bookRequest.getEdition());
        book.setLanguage(bookRequest.getLanguage());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setNumberOfPage(bookRequest.getNumberOfPage());
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setQuantity(bookRequest.getQuantity());
        book.setPrice(bookRequest.getPrice());
        book.setCreateAt(LocalDateTime.now());
        book.setStatus("ACTIVE");
        book.setImage(bookRequest.getImage());

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorId()));
        book.setAuthors(authors);

        return  bookRepository.save(book);

    }

    @Transactional
    public Book updateBook(Long bookId, BookRequest bookRequest) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Publisher publisher = publisherRepository.findById(bookRequest.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (bookRequest.getBookName() != null &&
                !existingBook.getBookName().equals(bookRequest.getBookName())) {
            existingBook.setBookName(bookRequest.getBookName());
        }
        if (bookRequest.getTitle() != null &&
                !existingBook.getTitle().equals(bookRequest.getTitle())) {
            existingBook.setTitle(bookRequest.getTitle());
        }
        if (bookRequest.getEdition() != 0 &&
                existingBook.getEdition() != (bookRequest.getEdition())) {
            existingBook.setEdition(bookRequest.getEdition());
        }
        if (bookRequest.getLanguage() != null &&
                !existingBook.getLanguage().equals(bookRequest.getLanguage())) {
            existingBook.setLanguage(bookRequest.getLanguage());
        }
        if (bookRequest.getPublicationYear() != 0 &&
                existingBook.getPublicationYear() != (bookRequest.getPublicationYear())) {
            existingBook.setPublicationYear(bookRequest.getPublicationYear());
        }
        if (bookRequest.getNumberOfPage() != 0 &&
                existingBook.getNumberOfPage() != bookRequest.getNumberOfPage()) {
            existingBook.setNumberOfPage(bookRequest.getNumberOfPage());
        }
        if (category != null &&
                !existingBook.getCategory().equals(category)) {
            existingBook.setCategory(category);
        }
        if (publisher != null &&
                !existingBook.getPublisher().equals(publisher)) {
            existingBook.setPublisher(publisher);
        }
        if (bookRequest.getQuantity() != 0 &&
                existingBook.getQuantity() != bookRequest.getQuantity()) {
            existingBook.setQuantity(bookRequest.getQuantity());
        }
        if (bookRequest.getPrice() != null &&
                existingBook.getPrice() != bookRequest.getPrice()) {
            existingBook.setPrice(bookRequest.getPrice());
        }
        if (bookRequest.getImage() != null &&
                !existingBook.getImage().equals(bookRequest.getImage())) {
            existingBook.setImage(bookRequest.getImage());
        }
        if (bookRequest.getStatus() != null &&
                !existingBook.getStatus().equals(bookRequest.getStatus())) {
            existingBook.setStatus(bookRequest.getStatus());
        }

        existingBook.setUpdateAt(LocalDateTime.now());

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorId()));
        if (authors != null && !existingBook.getAuthors().equals(authors)) {
            existingBook.setAuthors(authors);
        }

        return bookRepository.save(existingBook);
    }



}
