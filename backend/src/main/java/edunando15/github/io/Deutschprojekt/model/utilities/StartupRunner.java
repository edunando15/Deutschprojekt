package edunando15.github.io.Deutschprojekt.model.utilities;

import edunando15.github.io.Deutschprojekt.model.constants.ProjectConstants;
import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.services.TermService;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StartupRunner implements CommandLineRunner {
    private final TermService service;
    private final FileParser parser;

    public StartupRunner(TermService service, FileParser parser) {
        this.service = service;
        this.parser = parser;
    }

    @Override
    public  void run(String @Nullable ... args) throws Exception {
        Set<String> existingWords = service.getAll().stream()
                .parallel()
                .map(Term::getWord)
                .collect(Collectors.toSet());
        List<Term> missingTerms = new ArrayList<>();
        for (Term term : parser.getTerms(new File(ProjectConstants.A2_B1_CSV_FILE_PATH))) {
            if (existingWords.add(term.getWord())) {
                missingTerms.add(term);
            }
        }
        if (!missingTerms.isEmpty()) {
            service.addAll(missingTerms);
        }
    }
}
