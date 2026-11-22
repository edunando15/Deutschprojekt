package edunando15.github.io.Deutschprojekt.model.entities;

/**
 * Enumeration representing a German article.
 */
public enum Article {
    Der,
    Die,
    Das;

    static public Article fromString(String articleStr) {
        for (Article article : Article.values()) {
            if (article.name().equalsIgnoreCase(articleStr)) {
                return article;
            }
        }
        throw new IllegalArgumentException("Unknown article: " + articleStr);
    }
}
