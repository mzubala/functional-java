package pl.com.bottega.functional.basics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;

class DictionaryLoader {

    private final URI path;

    DictionaryLoader(URI path) {
        this.path = path;
    }

    Collection<String> load() {
        try (var reader = getReader()) {
            String line;
            var words = new HashSet<String>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                var lineWords = line.split(", ");
                for (int i = 0; i < lineWords.length; i++) {
                    lineWords[i] = lineWords[i].trim();
                }
                for(String word : lineWords) {
                    if(isUsable(word)) {
                        words.add(word);
                    }
                }
            }
            return words;
        } catch (Exception ex) {
            throw new RuntimeException("Failed to read the dictionary.");
        }
    }

    private BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(new File(path)));
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
