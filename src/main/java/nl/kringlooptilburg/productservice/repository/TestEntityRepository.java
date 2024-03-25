package nl.kringlooptilburg.productservice.repository;

import nl.kringlooptilburg.productservice.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, Integer> {
}
