package edunando15.github.io.Deutschprojekt.controller;

import edunando15.github.io.Deutschprojekt.model.entities.Article;
import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.services.TermService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TermsControllerTest {

    private TermService termService;
    private TermsController controller;
    private List<Term> terms;

    @BeforeEach
    void setUp() {
        termService = mock(TermService.class);
        controller = new TermsController(termService);
        terms = IntStream.rangeClosed(1, 30)
                .mapToObj(index -> {
                    Term term = new Term(Article.Das, "Wort" + index, "word " + index, "Wörter" + index);
                    term.setId((long) index);
                    return term;
                })
                .toList();
        when(termService.getAll()).thenReturn(terms);
    }

    @Test
    void startQuizReturnsThirtyPromptsWithoutAnswers() {
        List<TermsController.QuizQuestion> questions = controller.startQuiz();

        assertEquals(30, questions.size());
        assertEquals(30, questions.stream().parallel().map(TermsController.QuizQuestion::termId).distinct().count());
        assertTrue(questions.stream().parallel().allMatch(question -> question.englishTranslation().startsWith("word ")));
    }

    @Test
    void startQuizDoesNotRepeatWordsFromDuplicateSeedRows() {
        Term duplicate = new Term(Article.Der, "Wort1", "duplicate translation", "Wörter");
        duplicate.setId(31L);
        List<Term> termsWithDuplicate = new ArrayList<>(terms);
        termsWithDuplicate.add(duplicate);
        when(termService.getAll()).thenReturn(termsWithDuplicate);

        List<TermsController.QuizQuestion> questions = controller.startQuiz();

        assertEquals(30, questions.size());
        assertEquals(30, questions.stream().parallel().map(TermsController.QuizQuestion::englishTranslation).distinct().count());
    }

    @Test
    void startQuizFailsWhenThereAreFewerThanThirtyTerms() {
        when(termService.getAll()).thenReturn(terms.subList(0, 29));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                controller::startQuiz);

        assertEquals(503, exception.getStatusCode().value());
    }

    @Test
    void submitQuizScoresAnswersAndReturnsCorrections() {
        List<TermsController.QuizAnswer> answers = new ArrayList<>(terms.stream()
                .parallel()
                .map(term -> new TermsController.QuizAnswer(
                        term.getId(),
                        "das",
                        term.getWord(),
                        term.getPlural()))
                .toList());
        answers.set(0, new TermsController.QuizAnswer(1L, "der", "falsch", "falsch"));

        TermsController.QuizResult result = controller.submitQuiz(answers);

        assertEquals(29, result.score());
        assertEquals(30, result.total());
        assertEquals(30, result.results().size());
        assertFalse(result.results().get(0).correct());
        assertEquals("Wort1", result.results().get(0).correctWord());
        assertTrue(result.results().get(1).correct());
    }

    @Test
    void submitQuizRejectsIncompleteSubmissions() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> controller.submitQuiz(List.of()));

        assertEquals(400, exception.getStatusCode().value());
    }
}
