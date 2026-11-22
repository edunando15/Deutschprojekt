package edunando15.github.io.Deutschprojekt.model.utilities;

import edunando15.github.io.Deutschprojekt.model.entities.Term;

/**
 * This class is used to check che correctness of terms.
 */
public class TermChecker {

    public TermChecker() {}

    /**
     * This method is used to check whether a term is correct, compared to the expected term.
     * @param term1 The correct term.
     * @param term2 The term to check.
     * @return True if both terms are equal, false otherwise.
     */
    public boolean check(Term term1, Term term2) {
        return term1.equalsWithoutTranslation(term2);
    }

}
