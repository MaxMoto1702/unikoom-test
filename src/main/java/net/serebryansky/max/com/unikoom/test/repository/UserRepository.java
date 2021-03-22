package net.serebryansky.max.com.unikoom.test.repository;

import net.serebryansky.max.com.unikoom.test.domain.User;
import net.serebryansky.max.com.unikoom.test.domain.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Репозиторий пользователей
 * <p>
 * REST API для сущности пользователя
 */
@RepositoryRestResource(excerptProjection = UserProjection.class)
public interface UserRepository extends JpaRepository<User, Long> {
}
