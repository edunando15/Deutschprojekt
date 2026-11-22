package edunando15.github.io.Deutschprojekt.model.repositories;

import edunando15.github.io.Deutschprojekt.model.entities.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermRepository extends JpaRepository<Term, String> {}
