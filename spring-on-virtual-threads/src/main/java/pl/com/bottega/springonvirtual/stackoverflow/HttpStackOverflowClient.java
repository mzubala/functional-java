package pl.com.bottega.springonvirtual.stackoverflow;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpStackOverflowClient implements StackOverflowClient {

    private final RestTemplate soRestTemplate;

    @Override
    public String mostRecentQuestionAbout(String tag) {
        var body = soRestTemplate.getForEntity("/questions/tagged/" + tag, String.class).getBody();
        return Jsoup.parse(body).select(".s-post-summary a.s-link").get(0).text();
    }
}

@Configuration
class HttpStackOverflowClientConfig {

    @Bean
    RestTemplate soRestTemplate() {
        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return new RestTemplateBuilder()
                .rootUri("https://stackoverflow.com")
                .setConnectTimeout(Duration.ofMillis(1000))
                .setReadTimeout(Duration.ofMillis(10000))
                .messageConverters(stringHttpMessageConverter)
                .build();
    }

}
