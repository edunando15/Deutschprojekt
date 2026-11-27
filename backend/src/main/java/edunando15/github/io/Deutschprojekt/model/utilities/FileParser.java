package edunando15.github.io.Deutschprojekt.model.utilities;

import edunando15.github.io.Deutschprojekt.model.entities.Article;
import edunando15.github.io.Deutschprojekt.model.entities.Term;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class FileParser {

    private final ArrayList<Term> terms;

    public FileParser() {
        this.terms = new ArrayList<>();
    }

    public ArrayList<Term> getTerms(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int i = 0;
        while((line = reader.readLine()) != null) {
            if (i == 0) {
                i++;
                continue; // Skip header line
            }
            String[] tokens = line.split(",");
            Term term = new Term(Article.fromString(tokens[0]), tokens[1], tokens[2], tokens[3]);
            terms.add(term);
        }
        return terms;
    }
}
