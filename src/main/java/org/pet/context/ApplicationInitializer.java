package org.pet.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServiceLocator.registerService(new CurrencyValidator());
        ServiceLocator.registerService(new ObjectMapper());
        ServiceLocator.registerService(new BuilderRequestDto());

    }
}
