package guru.qa.niffler.api.interceptor;

import guru.qa.niffler.api.context.CookieContext;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import javax.annotation.Nullable;
import java.io.IOException;

public class AddCookieInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        CookieContext cookieContext = CookieContext.getInstance();
        Request originalRequest = chain.request();
        Headers.Builder builder = originalRequest.headers().newBuilder();

        @Nullable String jsessionIdCookie = cookieContext.getJSessionIdCookieValue();
        @Nullable String xsrfTokenCookie = cookieContext.getXsrfTokenCookieValue();

        if (jsessionIdCookie != null || xsrfTokenCookie != null) {
            builder.removeAll("Cookie");

            String resultCookie = "";

            if (jsessionIdCookie != null) {
                resultCookie = cookieContext.getJSessionIdFormattedCookie() + ";";
            }
            if (xsrfTokenCookie != null) {
                resultCookie  = resultCookie + cookieContext.getXsrfTokenFormattedCookie();
            }


            builder.add("Cookie", resultCookie);
        }

        Headers newHeaders = builder.build();

        return chain.proceed(originalRequest.newBuilder()
                .url(originalRequest.url())
                .headers(newHeaders)
                .method(originalRequest.method(), originalRequest.body())
                .build()
        );
    }
}
