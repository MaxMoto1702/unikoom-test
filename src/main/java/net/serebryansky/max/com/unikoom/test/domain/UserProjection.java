package net.serebryansky.max.com.unikoom.test.domain;

import org.springframework.data.rest.core.config.Projection;

/**
 * Ограничивает список атирибутов выдаемых для пользователя в списке
 */
@Projection(types = User.class)
public interface UserProjection {
    /**
     * {@link User#id}
     */
    Long getId();

    /**
     * {@link  User#username}
     */
    String getUsername();

    /**
     * {@link  User#name}
     */
    String getName();
}
