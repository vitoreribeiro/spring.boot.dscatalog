package spring.boot.dscatalog.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import spring.boot.dscatalog.dtos.CategoryDTO;
import spring.boot.dscatalog.dtos.ProductDTO;
import spring.boot.dscatalog.entities.Category;
import spring.boot.dscatalog.entities.Product;
import spring.boot.dscatalog.repositories.CategoryRepository;
import spring.boot.dscatalog.repositories.ProductRepository;
import spring.boot.dscatalog.services.exceptions.DatabaseException;
import spring.boot.dscatalog.services.exceptions.ResourceNotFoundException;

import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository repository;

    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository repository,
                          CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable){
        Page<Product> list = repository.findAll(pageable);
        return list.map(ProductDTO::new);
    }
    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
        dtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    private void dtoToEntity(ProductDTO dto, Product entity){

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();
        for(CategoryDTO catDto : dto.getCategories()){
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }

    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            dtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        }
        catch(EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
