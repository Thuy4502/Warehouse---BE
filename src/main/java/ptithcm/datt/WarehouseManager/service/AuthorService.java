package ptithcm.datt.WarehouseManager.service;

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

}
