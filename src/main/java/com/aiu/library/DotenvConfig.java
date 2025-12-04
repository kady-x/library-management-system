package com.aiu.library;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

public class DotenvConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, org.springframework.boot.SpringApplication application) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        Map<String, Object> envVars = dotenv.entries().stream()
            .collect(java.util.stream.Collectors.toMap(
                entry -> entry.getKey(),
                entry -> entry.getValue()
            ));

        environment.getPropertySources().addFirst(
            new MapPropertySource("dotenv", envVars)
        );
    }
}
