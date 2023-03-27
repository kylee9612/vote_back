package com.axiasoft.vote_back.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile(value="local")
@PropertySource("classpath:/config/application-local.yml")
public class ProfileLocal {
}
