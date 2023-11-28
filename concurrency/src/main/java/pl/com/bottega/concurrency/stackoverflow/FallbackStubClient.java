package pl.com.bottega.concurrency.stackoverflow;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

@Slf4j
public class FallbackStubClient implements StackOverflowClient {

	private final StackOverflowClient target;

	public FallbackStubClient(StackOverflowClient target) {
		this.target = target;
	}

	@Override
	public String mostRecentQuestionAbout(String tag) {
		try {
			return target.mostRecentQuestionAbout(tag);
		} catch (Exception e) {
			log.warn("Problem retrieving tag {}", tag);
			return switch(tag) {
				case "java" -> "How to generate xml report with maven depencency?";
				case "scala" -> "Update a timestamp SettingKey in an sbt 0.12 task";
				case "groovy" -> "Reusing Grails variables inside Config.groovy";
				case "clojure" ->  "Merge two comma delimited strings in Clojure";
				default -> STR."No info about \{tag}";
			};
		}
	}

	@Override
	public Document mostRecentQuestionsAbout(String tag) {
		try {
			return target.mostRecentQuestionsAbout(tag);
		} catch (Exception e) {
			log.warn("Problem retrieving recent question {}", tag, e);
			return loadStubHtmlFromDisk(tag);
		}
	}

	private Document loadStubHtmlFromDisk(String tag) {
		try {
			final URL resource = getClass().getResource("/" + tag + "-questions.html");
			final String html = IOUtils.toString(resource);
			return Jsoup.parse(html);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
