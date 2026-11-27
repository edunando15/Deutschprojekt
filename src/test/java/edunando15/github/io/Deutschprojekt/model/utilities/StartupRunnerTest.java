package edunando15.github.io.Deutschprojekt.model.utilities;

import edunando15.github.io.Deutschprojekt.model.entities.Article;
import edunando15.github.io.Deutschprojekt.model.entities.Term;
import edunando15.github.io.Deutschprojekt.model.services.TermService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StartupRunnerTest {

    @Test
    void onlyAddsVocabularyWordsThatAreNotAlreadyStored() throws Exception {
        Term existing = new Term(Article.Das, "Haus", "house", "Häuser");
        Term missing = new Term(Article.Der, "Hund", "dog", "Hunde");
        Term duplicate = new Term(Article.Der, "Hund", "hound", "Hunde");
        TermService service = mock(TermService.class);
        FileParser parser = mock(FileParser.class);
        when(service.getAll()).thenReturn(List.of(existing));
        when(parser.getTerms(any(File.class)))
                .thenReturn(new ArrayList<>(List.of(existing, missing, duplicate)));

        new StartupRunner(service, parser).run();

        verify(service).addAll(List.of(missing));
    }
}
