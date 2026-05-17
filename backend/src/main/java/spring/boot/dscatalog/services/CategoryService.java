package spring.boot.dscatalog.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.dscatalog.entities.Category;
import spring.boot.dscatalog.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Category findById(Long id){
      return repository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll(){
        return repository.findAll();
    }
}
