package pl.com.bottega.functional.basics.scrable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class Dictionary {
    private final List<String> words;

    Dictionary(Collection<String> words) {
        this.words = new LinkedList<>(words);
        this.words.sort(Comparator.comparing(String::length));
    }

    List<String> findPossibleWords(List<Character> letters) {
        var possibleWords = new LinkedList<String>();
        for (String word : words) {
            if (word.length() > letters.size()) {
                 break;
            }
            if (wordCanBeCreatedUsingLetters(word, letters)) {
                possibleWords.add(word);
            }
        }
        possibleWords.sort(Comparator.comparing(String::length).reversed());
        return possibleWords;
    }

    private boolean wordCanBeCreatedUsingLetters(String word, List<Character> letters) {
        List<Character> wordChars = new ArrayList<>();
        for(Character c: word.toCharArray()) {
            wordChars.add(c);
        }
        List<Character> lettersUsed = new ArrayList<>(letters);
        for (var i = lettersUsed.iterator(); i.hasNext();) {
            var c = i.next();
            if(wordChars.contains(c)) {
                i.remove();
                wordChars.remove(c);
            }
        }
        for(var c : lettersUsed) {
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
