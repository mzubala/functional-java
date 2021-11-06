package pl.com.bottega.functional.basics.scrable;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class DictionaryLoaderTest {
    @Test
    void loadsWordsFromAFile() throws URISyntaxException {
        // given
        var loader = new DictionaryLoader(getClass().getResource("/testDictionary.txt").toURI());

        // expect
        assertThat(loader.load()).containsExactlyInAnyOrder("be", "biały", "białego", "białemu", "białek", "białko");
    }
}
