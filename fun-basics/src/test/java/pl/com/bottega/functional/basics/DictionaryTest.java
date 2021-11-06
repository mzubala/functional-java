package pl.com.bottega.functional.basics;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DictionaryTest {
    private final Dictionary dictionary = new Dictionary(List.of(
        "ala", "kot", "miasto", "lal", "sit", "sito", "sitom"
    ));

    @Test
    void returnsListOfPossibleWords() {
        assertThat(dictionary.findPossibleWords(List.of('a', 'a', 'l', 'l'))).containsExactly("ala", "lal");
        assertThat(dictionary.findPossibleWords(List.of('t', 'i', 's', 'o', 'k', 'm')))
            .containsExactly("sitom", "sito", "kot", "sit");
        assertThat(dictionary.findPossibleWords(List.of('r', 'a', 'z'))).isEmpty();
        assertThat(dictionary.findPossibleWords(List.of('*', 'a', 'k', 't', 'm', 's', '*')))
            .containsExactly("miasto", "sitom", "sito", "ala", "kot", "lal", "sit");
    }
}
