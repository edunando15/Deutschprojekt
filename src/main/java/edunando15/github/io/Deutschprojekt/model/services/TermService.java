package edunando15.github.io.Deutschprojekt.model.services;

import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.repositories.TermRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TermService {

    private final TermRepository repository;

    public TermService(TermRepository repository) {
        this.repository = repository;
    }

    public Term create(Term term) {
        return repository.save(term);
    }

    public void createAll(List<Term> terms) {
        repository.saveAll(terms);
    }

    public Optional<Term> getById(String id) {
        return repository.findById(id);
    }

    public Optional<Term> getByWord(String word) {
        String trimmedWord = word.trim();
        return repository.findAll().stream()
                .parallel()
                .filter(term -> term.getWord().equals(trimmedWord))
                .findFirst();
    }

    public List<Term> getAll() {
        return repository.findAll();
    }

    public Term update(Term term) {
        return repository.save(term);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
