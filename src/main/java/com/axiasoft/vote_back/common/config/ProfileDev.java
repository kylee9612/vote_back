package com.axiasoft.vote_back.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile(value="dev")
@PropertySource("classpath:/config/application-dev.yml")
public class ProfileDev {
}
