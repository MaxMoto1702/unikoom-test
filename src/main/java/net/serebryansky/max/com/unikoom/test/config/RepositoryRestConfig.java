package net.serebryansky.max.com.unikoom.test.config;

import net.serebryansky.max.com.unikoom.test.domain.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.springframework.http.HttpMethod.*;

@Configuration
public class RepositoryRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config
                .setDefaultMediaType(MediaType.APPLICATION_JSON)
                .exposeIdsFor(User.class)
                .getExposureConfiguration()
                .forDomainType(User.class)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(PATCH, PUT, DELETE));
    }
}
