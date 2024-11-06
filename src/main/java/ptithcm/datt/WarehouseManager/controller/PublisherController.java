package ptithcm.datt.WarehouseManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.datt.WarehouseManager.model.Publisher;
import ptithcm.datt.WarehouseManager.response.EntityResponse;
import ptithcm.datt.WarehouseManager.service.PublisherService;

import java.util.List;
import java.util.concurrent.Flow;

@RestController
@RequestMapping("/v1/publisher")
public class PublisherController {
    @Autowired
    private PublisherService publisherService;

    @RequestMapping("/getAll")
    public ResponseEntity<EntityResponse> getAllPublisher() {
        List<Publisher> publisherList = publisherService.getAllPublisher();
        EntityResponse<List<Publisher>> response = new EntityResponse<>();
        response.setData(publisherList);
        response.setMessage("Get all publisher successfully");
        response.setCode(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
