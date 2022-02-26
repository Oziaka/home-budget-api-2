package pl.user.friend_ship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

    @Query("SELECT f FROM friend_ship f LEFT OUTER JOIN f.user u1 LEFT OUTER JOIN f.user2 u2 WHERE u1 = :user AND u2 = :user2")
    Optional<FriendShip> findByUserAndUser2(@Param("user") User user, @Param("user2") User user2);

    List<FriendShip> findALlByUser(User user);

    Optional<FriendShip> findByUserAndId(User user, Long friendShipId);

}
