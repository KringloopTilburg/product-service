package nl.kringlooptilburg.productservice.controller;

import nl.kringlooptilburg.productservice.model.TestEntity;
import nl.kringlooptilburg.productservice.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test-entity")
public class TestEntityController {

    @Autowired
    private TestEntityRepository testRepo;

    @GetMapping("/get")
    public List<TestEntity> getTestEntities() {
            return testRepo.findAll();
    }

    @PostMapping("/post")
    public TestEntity postTestEntity(TestEntity testEntity) {
        return testRepo.save(testEntity);
    }
}
