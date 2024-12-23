package ptithcm.datt.WarehouseManager.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.model.Author;
import ptithcm.datt.WarehouseManager.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    AuthorRepository authorRepository;

    public List<Author> getAllAuthor() {
        return authorRepository.findAll();
    }

    @Transactional
    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long authorId, Author authorRequest) throws Exception {
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new Exception("Tác giả không tồn tại với ID: " + authorId));

        if (authorRequest.getAuthorName() != null && !authorRequest.getAuthorName().equals(existingAuthor.getAuthorName())) {
            existingAuthor.setAuthorName(authorRequest.getAuthorName());
        }

        if (authorRequest.getPhoneNumber() != null && !authorRequest.getPhoneNumber().equals(existingAuthor.getPhoneNumber())) {
            existingAuthor.setPhoneNumber(authorRequest.getPhoneNumber());
        }

        if (authorRequest.getEmail() != null && !authorRequest.getEmail().equals(existingAuthor.getEmail())) {
            existingAuthor.setEmail(authorRequest.getEmail());
        }
        return authorRepository.save(existingAuthor);
    }


}
