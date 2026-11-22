package edunando15.github.io.Deutschprojekt.model.repositories;

import edunando15.github.io.Deutschprojekt.model.entities.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    // It is important to note that the method name must follow the naming
    // convention of Spring Data JPA to work correctly. The method name should be
    // in the format of "findBy" followed by the property name of the entity.
    // In this case, we are searching for a Term entity by its "word" property,
    // so the method name is "findByWord".
    Optional<Term> findByWord(String word);
}
