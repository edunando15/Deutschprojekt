package edunando15.github.io.Deutschprojekt.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Class representing a Term.
 */
@Entity
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Article article;

    private String word;

    private String englishTranslation;

    private String plural;

    /**
     * Constructor for a term.
     * @param article The article of the term.
     * @param word The actual term.
     * @param englishTranslation Translation of the term in another language.
     * @param plural Optional: plural of the term.
     */
    public Term(Article article, String word, String englishTranslation, String plural) {
        this.article = article;
        this.word = word.trim();
        this.englishTranslation = englishTranslation.trim();
        this.plural = plural.trim();
    }

    public Term() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Term term = (Term) o;
        return this.article.equals(term.article) &&
                this.word.equals(term.word.trim()) &&
                this.englishTranslation.equalsIgnoreCase(term.englishTranslation) &&
                this.plural.equals(term.plural.trim());
    }

    public boolean equalsWithoutTranslation(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Term term = (Term) o;
        return this.article.equals(term.article) &&
                this.word.equalsIgnoreCase(term.word.trim()) &&
                this.plural.equalsIgnoreCase(term.plural.trim());
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public  void setEnglishTranslation(String englishTranslation) {
        this.englishTranslation = englishTranslation;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
