package spring.boot.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.dscatalog.entities.Category;
import spring.boot.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

    @Autowired private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Category findById(Long id){
      return new Category(1L, "category 1");
    }
}
