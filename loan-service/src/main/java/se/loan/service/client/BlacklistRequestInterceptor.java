package se.loan.service.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import static se.loan.service.LoanServiceApplication.REPOSITORY_BASE_PATH;

//TODO workaround to send empty body
//explore why @Body("%7B %7D") is ignored
@Component
public class BlacklistRequestInterceptor implements RequestInterceptor {

    private static final String EMPTY_BODY = "{}";

    @Override
    public void apply(RequestTemplate template) {
        if (template.method().equals("PUT") && template.url().startsWith(REPOSITORY_BASE_PATH + "/blacklists/")) {
            template.body(EMPTY_BODY);
        }
    }
}
