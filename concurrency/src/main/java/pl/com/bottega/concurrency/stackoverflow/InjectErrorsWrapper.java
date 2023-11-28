package pl.com.bottega.concurrency.stackoverflow;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class InjectErrorsWrapper implements StackOverflowClient {

	private final StackOverflowClient target;
	private final Set<String> blackList;

	public InjectErrorsWrapper(StackOverflowClient target, String... blackList) {
		this.target = target;
		this.blackList = new HashSet<>(Arrays.asList(blackList));
	}

	@Override
	public String mostRecentQuestionAbout(String tag) {
		throwIfBlackListed(tag);
		return target.mostRecentQuestionAbout(tag);
	}

	@Override
	public Document mostRecentQuestionsAbout(String tag) {
		throwIfBlackListed(tag);
		return target.mostRecentQuestionsAbout(tag);
	}

	private void throwIfBlackListed(String tag) {
		if (blackList.isEmpty() || blackList.contains(tag)) {
			ArtificialSleepWrapper.artificialSleep(400);
			log.warn("About to throw artifical exception due to: {}", tag);
			throw new IllegalArgumentException("Unsupported " + tag);
		}
	}

}
