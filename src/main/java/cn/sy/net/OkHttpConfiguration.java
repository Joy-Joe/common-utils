package cn.sy.net;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfiguration {
    @Value("${ok.http.connect-timeout: 5}")
    private Integer connectTimeout;

    @Value("${ok.http.read-timeout: 5}")
    private Integer readTimeout;

    @Value("${ok.http.write-timeout: 5}")
    private Integer writeTimeout;

    @Value("${ok.http.max-idle-connections: 300}")
    private Integer maxIdleConnections;

    @Value("${ok.http.keep-alive-duration: 10}")
    private Long keepAliveDuration;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectionPool(pool())
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .hostnameVerifier((hostname, session) -> true)
                .build();
    }

    @Bean
    public ConnectionPool pool() {
        return new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);
    }
}
