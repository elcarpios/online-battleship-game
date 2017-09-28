package salvo.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
    /* 4.4.2 Query creation
    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
     */
    List<Player> findByUserName(@Param("userName") String userName);
}
