package edunando15.github.io.Deutschprojekt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import edunando15.github.io.Deutschprojekt.model.entities.Article;
import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.services.TermService;
import edunando15.github.io.Deutschprojekt.model.utilities.TermChecker;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DeutschprojektApplicationTests {

	@Autowired
	private TermService service;

	@Test
	void fetchTest() {
		Term haus = new Term(Article.Das, "Haus", "house", "Häuser");
		TermChecker checker = new TermChecker();
		Optional<Term> optionalHaus = service.getByWord("Haus");
		assertTrue(optionalHaus.isPresent());
		assertTrue(checker.check(optionalHaus.get(), haus));
		Term hund = new Term(Article.Der, "Hund", "dog", "Hunde");
		Optional<Term> optionalHund = service.getByWord("Hund");
		assertTrue(optionalHund.isPresent());
		assertTrue(checker.check(optionalHund.get(), hund));
		assertFalse(checker.check(optionalHund.get(), haus));
	}

}
