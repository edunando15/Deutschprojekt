package edunando15.github.io.Deutschprojekt.controller;

import edunando15.github.io.Deutschprojekt.model.entities.Article;
import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.services.TermService;
import edunando15.github.io.Deutschprojekt.model.utilities.TermChecker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v0/terms")
public class TermsController {

    private static final int QUIZ_SIZE = 30;

    private final TermService termService;
    private final TermChecker termChecker;

    public TermsController(TermService termService) {
        this.termService = termService;
        this.termChecker = new TermChecker();
    }

    @GetMapping("/quiz")
    public List<QuizQuestion> startQuiz() {
        Map<String, Term> uniqueTerms = termService.getAll().stream()
                .parallel()
                .collect(Collectors.toMap(
                        Term::getWord,
                        Function.identity(),
                        (first, duplicate) -> first,
                        LinkedHashMap::new));
        List<Term> terms = new ArrayList<>(uniqueTerms.values());
        if (terms.size() < QUIZ_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "At least 30 terms are required to start a quiz");
        }

        Collections.shuffle(terms);
        return terms.stream()
                .parallel()
                .limit(QUIZ_SIZE)
                .map(term -> new QuizQuestion(term.getId(), term.getEnglishTranslation()))
                .toList();
    }

    @PostMapping("/quiz/submit")
    public QuizResult submitQuiz(@RequestBody List<QuizAnswer> answers) {
        if (answers == null || answers.size() != QUIZ_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A quiz submission must contain exactly 30 answers");
        }
        if (answers.stream().anyMatch(answer -> answer == null || answer.termId() == null)
                || answers.stream().map(QuizAnswer::termId).distinct().count() != QUIZ_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Each answer must contain a distinct term ID");
        }

        Map<Long, Term> termsById = termService.getAll().stream()
                .parallel()
                .collect(Collectors.toMap(Term::getId, Function.identity()));
        List<AnswerResult> results = answers.stream()
                .parallel()
                .map(answer -> {
                    Term expected = termsById.get(answer.termId());
                    if (expected == null) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "The submission contains an unknown term ID");
                    }
                    boolean correct = isCorrect(answer, expected);
                    return new AnswerResult(
                            answer.termId(),
                            expected.getEnglishTranslation(),
                            correct,
                            expected.getArticle().name(),
                            expected.getWord(),
                            expected.getPlural());
                })
                .toList();

        int score = (int) results.stream().parallel().filter(AnswerResult::correct).count();
        return new QuizResult(score, QUIZ_SIZE, results);
    }

    private boolean isCorrect(QuizAnswer answer, Term expected) {
        if (answer.article() == null || answer.word() == null || answer.plural() == null) {
            return false;
        }

        Article article;
        try {
            article = Article.fromString(answer.article().trim());
        } catch (IllegalArgumentException exception) {
            return false;
        }

        Term submitted = new Term(article, answer.word(), "", answer.plural());
        return termChecker.check(expected, submitted);
    }

    public record QuizQuestion(Long termId, String englishTranslation) {}

    public record QuizAnswer(Long termId, String article, String word, String plural) {}

    public record AnswerResult(
            Long termId,
            String englishTranslation,
            boolean correct,
            String correctArticle,
            String correctWord,
            String correctPlural) {}

    public record QuizResult(int score, int total, List<AnswerResult> results) {}
}
