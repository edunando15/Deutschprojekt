package edunando15.github.io.Deutschprojekt.model.services;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class GenericService<T> {

    private final JpaRepository<T, Long> repository;

    protected GenericService(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    public T add(T entity) {
        return repository.save(entity);
    }

    public List<T> addAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
