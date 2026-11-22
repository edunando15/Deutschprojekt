package edunando15.github.io.Deutschprojekt.model.services;

import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.repositories.TermRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TermService extends GenericService<Term> {

    private final TermRepository termRepository;

    public TermService(TermRepository repository) {
        super(repository);
        this.termRepository = repository;
    }

    public Optional<Term> getByWord(String word) {
        return getByString(word);
    }

    public Optional<Term> getByString(String value) {
        return termRepository.findByWord(value.trim());
    }
}
