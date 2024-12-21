package ptithcm.datt.WarehouseManager.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.exception.BookException;
import ptithcm.datt.WarehouseManager.model.*;
import ptithcm.datt.WarehouseManager.repository.*;
import ptithcm.datt.WarehouseManager.request.BookExcelRequest;
import ptithcm.datt.WarehouseManager.request.BookRequest;
import ptithcm.datt.WarehouseManager.response.TopSellingBooksResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private CategoryBookRepository categoryBookRepository;

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
            // Tìm kiếm Publisher
            Publisher publisher = publisherRepository.findByPublisherName(bookRequest.getPublisherName())
                    .orElseThrow(() -> new RuntimeException("Publisher not found: " + bookRequest.getPublisherName()));

            // Tạo Book mới
            Book book = new Book();
            book.setBookName(bookRequest.getBookName());
            book.setTitle(bookRequest.getTitle());
            book.setEdition(bookRequest.getEdition());
            book.setLanguage(bookRequest.getLanguage());
            book.setPublicationYear(bookRequest.getPublicationYear());
            book.setNumberOfPage(bookRequest.getNumberOfPage());
            book.setQuantity(bookRequest.getQuantity());
            book.setPrice(bookRequest.getPrice());
            book.setISBN(bookRequest.getISBN());
            book.setCreateAt(LocalDateTime.now());
            book.setStatus("ACTIVE");
            book.setImage(bookRequest.getImage());
            book.setPublisher(publisher);

            // Lưu Book trước
            book = bookRepository.save(book);

            // Xử lý các thể loại
            List<Category> categories = categoryRepository.findByCategoryNameIn(bookRequest.getCategoryNames());
            Set<CategoryBook> categoryBooks = new HashSet<>();
            for (Category category : categories) {
                CategoryBook categoryBook = new CategoryBook();
                categoryBook.setBook(book);  // book đã được lưu
                categoryBook.setCategory(category);
                categoryBooks.add(categoryBook);
            }

            // Lưu các mối quan hệ CategoryBook
            categoryBookRepository.saveAll(categoryBooks);

            // Xử lý các tác giả
            Set<Author> authors = new HashSet<>();
            for (String authorName : bookRequest.getAuthorName()) {
                Author author = authorRepository.findByAuthorName(authorName)
                        .orElseGet(() -> {
                            // Tạo mới Author nếu chưa tồn tại
                            Author newAuthor = new Author();
                            newAuthor.setAuthorName(authorName);
                            return authorRepository.save(newAuthor);
                        });
                authors.add(author);
            }
            book.setAuthors(authors);

            // Lưu cập nhật lại Book với các mối quan hệ
            bookRepository.save(book);

            // Thêm Book vào danh sách kết quả
            books.add(book);
        }

        return books;
    }




    @Transactional
    public Book createBook(BookRequest bookRequest) {
        // Lấy nhà xuất bản từ ID
        Publisher publisher = publisherRepository.findById(bookRequest.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        // Lấy các thể loại từ danh sách ID thể loại
        List<Category> categories = categoryRepository.findAllById(bookRequest.getCategoryIds());
        if (categories.size() != bookRequest.getCategoryIds().size()) {
            throw new RuntimeException("Some categories not found");
        }

        // Tạo đối tượng sách mới
        Book book = new Book();
        book.setBookName(bookRequest.getBookName());
        book.setTitle(bookRequest.getTitle());
        book.setEdition(bookRequest.getEdition());
        book.setLanguage(bookRequest.getLanguage());
        book.setPublicationYear(bookRequest.getPublicationYear());
        book.setNumberOfPage(bookRequest.getNumberOfPage());
        book.setPublisher(publisher);
        book.setQuantity(bookRequest.getQuantity());
        book.setPrice(bookRequest.getPrice());
        book.setISBN(bookRequest.getISBN());
        book.setCreateAt(LocalDateTime.now());
        book.setStatus("ACTIVE");
        book.setImage(bookRequest.getImage());

        // Thiết lập các tác giả
        Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorIds()));
        book.setAuthors(authors);

        // Lưu sách vào cơ sở dữ liệu
        Book savedBook = bookRepository.save(book);

        // Tạo các mối quan hệ sách-thể loại trong bảng trung gian category_book
        for (Category category : categories) {
            CategoryBook categoryBook = new CategoryBook();
            categoryBook.setBook(savedBook);
            categoryBook.setCategory(category);
            categoryBookRepository.save(categoryBook);
        }

        return savedBook;
    }


    @Transactional
    public Book updateBook(Long bookId, BookRequest bookRequest) {
        // Tìm sách hiện tại
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Tìm nhà xuất bản
        Publisher publisher = publisherRepository.findById(bookRequest.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        // Cập nhật thông tin sách
        if (bookRequest.getBookName() != null && !existingBook.getBookName().equals(bookRequest.getBookName())) {
            existingBook.setBookName(bookRequest.getBookName());
        }
        if (bookRequest.getTitle() != null && !existingBook.getTitle().equals(bookRequest.getTitle())) {
            existingBook.setTitle(bookRequest.getTitle());
        }
        if (bookRequest.getEdition() != 0 && existingBook.getEdition() != bookRequest.getEdition()) {
            existingBook.setEdition(bookRequest.getEdition());
        }
        if (bookRequest.getLanguage() != null && !existingBook.getLanguage().equals(bookRequest.getLanguage())) {
            existingBook.setLanguage(bookRequest.getLanguage());
        }
        if (bookRequest.getPublicationYear() != 0 && existingBook.getPublicationYear() != bookRequest.getPublicationYear()) {
            existingBook.setPublicationYear(bookRequest.getPublicationYear());
        }
        if (bookRequest.getNumberOfPage() != 0 && existingBook.getNumberOfPage() != bookRequest.getNumberOfPage()) {
            existingBook.setNumberOfPage(bookRequest.getNumberOfPage());
        }
        if (bookRequest.getISBN() != null && !existingBook.getISBN().equals(bookRequest.getISBN())) {
            existingBook.setISBN(bookRequest.getISBN());
        }
        if (bookRequest.getQuantity() != 0 && existingBook.getQuantity() != bookRequest.getQuantity()) {
            existingBook.setQuantity(bookRequest.getQuantity());
        }
        if (bookRequest.getPrice() != null && !existingBook.getPrice().equals(bookRequest.getPrice())) {
            existingBook.setPrice(bookRequest.getPrice());
        }
        if (bookRequest.getImage() != null && !existingBook.getImage().equals(bookRequest.getImage())) {
            existingBook.setImage(bookRequest.getImage());
        }
        if (bookRequest.getStatus() != null && !existingBook.getStatus().equals(bookRequest.getStatus())) {
            existingBook.setStatus(bookRequest.getStatus());
        }

        if (!existingBook.getPublisher().equals(publisher)) {
            existingBook.setPublisher(publisher);
        }

        // Cập nhật danh sách thể loại nếu khác
        if (bookRequest.getCategoryIds() != null) {
            // Lấy danh sách categoryId hiện tại
            List<Long> currentCategoryIds = categoryBookRepository.findByBook(existingBook).stream()
                    .map(categoryBook -> categoryBook.getCategory().getCategoryId())
                    .toList();

            // So sánh danh sách thể loại mới với cũ
            if (!currentCategoryIds.equals(bookRequest.getCategoryIds())) {
                // Xóa liên kết cũ
                // Tìm danh sách CategoryBook liên quan đến Book
                List<CategoryBook> existingCategoryBooks = categoryBookRepository.findByBook(existingBook);
                categoryBookRepository.deleteAll(existingCategoryBooks);


                // Tạo liên kết mới
                for (Long categoryId : bookRequest.getCategoryIds()) {
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found"));

                    CategoryBook categoryBook = new CategoryBook();
                    categoryBook.setBook(existingBook);
                    categoryBook.setCategory(category);
                    categoryBookRepository.save(categoryBook);
                }
            }
        }

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorIds()));
        if (!existingBook.getAuthors().equals(authors)) {
            existingBook.setAuthors(authors);
        }

        existingBook.setUpdateAt(LocalDateTime.now());
        return bookRepository.save(existingBook);
    }


    public List<TopSellingBooksResponse> getTop3SellingBooks() {
        List<Object[]> results = bookRepository.findTopSellingBooks();
        return results.stream()
                .map(row -> new TopSellingBooksResponse(
                        (String) row[0],
                        (String) row[1],
                        (int) row[2],
                        (BigDecimal) row[3],
                        (double) row[4]))
                .collect(Collectors.toList());
    }



}
