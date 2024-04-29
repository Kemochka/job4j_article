package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SimpleArticleService implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleArticleService.class.getSimpleName());

    private final ArticleGenerator articleGenerator;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int count, Store<Article> articleStore) {
        LOGGER.info("Геренация статей в количестве {}", count);
        var words = wordStore.findAll();
        List<WeakReference<Article>> articles = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LOGGER.info("Сгенерирована статья № {}", i);
            Article article = articleGenerator.generate(words);
            articles.add(new WeakReference<>(article));
        }
        for (WeakReference<Article> weakArticle : articles) {
            Article article = weakArticle.get();
            if (article != null) {
                articleStore.save(article);
            }
        }
    }
}