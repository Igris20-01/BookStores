package uz.booker.bookstore.util;



import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ApiListUtil {

    public static final String[] SWAGGER_LIST = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/swagger-ui.html",
            "v3/api-docs/**",
            "v3/api-docs.yaml",
    };

     public static final String[]  WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/test",
            "/api/v1/register",
            "/api/v1/login/**",
            "/user/**",
            "/permission/**",
             "/orders/**",
             "/cart/**",
    };

     public static final String[] AUTH_LIST_URL = {
            "/api/v1/register",
            "/api/v1/login/**",
            "/api/v1/verify-account",
            "/api/v1/regenerate-otp",
            "/api/v1/forgot-password",
            "/api/v1/set-password",

    };

    public static final String[] BOOK_LIST_URL = {
            "/book/**",
            "/genre/**",
            "/author/**",
            "/review/**",
            "/feedback/**"
    };


    public static final String[] USER_ORDER_CART = {
            "/orders/**",
            "/cart/**",
            "/api/v1/test"
    };
}
