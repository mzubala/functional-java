package pl.com.bottega.springonvirtual.stackoverflow;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class LoadFromStackOverflowTask implements Callable<String> {

	private final StackOverflowClient client;
	private final String tag;

	public LoadFromStackOverflowTask(StackOverflowClient client, String tag) {
		this.client = client;
		this.tag = tag;
	}

	@Override
	public String call() throws Exception {
		return client.mostRecentQuestionAbout(tag);
	}
}
