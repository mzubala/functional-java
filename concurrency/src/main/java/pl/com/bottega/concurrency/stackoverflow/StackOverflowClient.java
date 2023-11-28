package pl.com.bottega.concurrency.stackoverflow;

import org.jsoup.nodes.Document;

public interface StackOverflowClient {

	String mostRecentQuestionAbout(String tag);
	Document mostRecentQuestionsAbout(String tag);

}
