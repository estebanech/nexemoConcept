package com.example.test01.demo.config;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.verify.VerifyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NexmoConfig {

    @Value("${app.nexmoKey}")
    private String nexmoKey;

    @Value("${app.nexmoSecret}")
    private String nexmoSecret;

    @Bean
    public NexmoClient nexmoClient() {
        return NexmoClient.builder().apiKey(nexmoKey).apiSecret(nexmoSecret).build();
    }

    @Bean
    public VerifyClient nexmoVerifyClient(NexmoClient nexmoClient) {
        return nexmoClient.getVerifyClient();
    }
}
