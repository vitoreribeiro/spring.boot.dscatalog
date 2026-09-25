package spring.boot.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.boot.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
