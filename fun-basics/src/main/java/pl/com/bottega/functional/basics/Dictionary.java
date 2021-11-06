package pl.com.bottega.functional.basics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Dictionary {
    private final Map<Integer, List<String>> words;

    Dictionary(Collection<String> words) {
        this.words = words.stream().collect(Collectors.groupingBy(String::length));
    }

    List<String> findPossibleWords(List<Character> letters) {
        return words.entrySet().stream().parallel()
            .filter((entry) -> entry.getKey() <= letters.size())
            .flatMap((entry) -> entry.getValue().stream())
            .filter((word) -> wordCanBeCreatedUsingLetters(word, letters))
            .sorted(Comparator.comparing(String::length).reversed())
            .collect(Collectors.toList());
    }

    private boolean wordCanBeCreatedUsingLetters(String word, List<Character> letters) {
        List<Character> wordChars = word.chars().mapToObj((i) -> (char) i).collect(Collectors.toList());
        List<Character> lettersToUse = new ArrayList<>(letters);
        for (var i = lettersToUse.iterator(); i.hasNext();) {
            var c = i.next();
            if(wordChars.contains(c)) {
                i.remove();
                wordChars.remove(c);
            }
        }
        for(var c : lettersToUse) {
            if(wordChars.size() == 0) {
                return true;
            }
            if(c == '*') {
                wordChars.remove(0);
            }
        }
        return wordChars.size() == 0;
    }
}
