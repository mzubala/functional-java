package pl.com.bottega.springonvirtual.stackoverflow;

import lombok.extern.slf4j.Slf4j;

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
}
