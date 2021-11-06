package pl.com.bottega.functional.basics;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DictionaryLoader {

    private final URI path;

    DictionaryLoader(URI path) {
        this.path = path;
    }

    Collection<String> load() {
        try (var lines = getLines()) {
            return lines.flatMap(this::wordsInLine).collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read the dictionary.");
        }
    }

    private Stream<String> wordsInLine(String line) {
        return Arrays.stream(line.split(", ")).map(String::trim).filter(this::isUsable);
    }

    private Stream<String> getLines() throws IOException {
        return Files.lines(Path.of(path));
    }

    private boolean isUsable(String word) {
        return word.length() > 1 && !startsWithCapitalLetter(word) && isSingleWord(word);
    }

    private boolean startsWithCapitalLetter(String word) {
        return word.matches("^[A-Z].*");
    }

    private boolean isSingleWord(String word) {
        return !word.contains(" ");
    }
}
