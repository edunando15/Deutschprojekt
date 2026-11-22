package edunando15.github.io.Deutschprojekt.model.utilities;

import edunando15.github.io.Deutschprojekt.model.constants.ProjectConstants;
import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.services.TermService;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.List;

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
        List<Term> terms = parser.getTerms(new File(ProjectConstants.A2_B1_CSV_FILE_PATH));
        service.addAll(terms);
    }
}
