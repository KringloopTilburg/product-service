package nl.kringlooptilburg.productservice.repositories;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Integer>{
}
