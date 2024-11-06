package ptithcm.datt.WarehouseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptithcm.datt.WarehouseManager.model.Publisher;
import ptithcm.datt.WarehouseManager.repository.PublisherRepository;

import java.util.List;

@Service
public class PublisherService {
    @Autowired
    private PublisherRepository publisherRepository;

    public List<Publisher> getAllPublisher() {
        return publisherRepository.findAll();
    }
}
