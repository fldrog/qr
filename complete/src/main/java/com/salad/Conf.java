package com.salad;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class Conf implements EnvironmentAware {

    private static Environment env;

    @Override
    public void setEnvironment(final Environment environment) {
        this.env = environment;
    }

    public static Environment getEnv() {
        return env;
    }

}