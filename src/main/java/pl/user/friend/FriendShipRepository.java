package pl.user.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {

  Optional<FriendShip> findFirstByUserAndUser2 (User user, User user2);


  @Query("SELECT f FROM FriendShip f WHERE f.user = :user OR f.user2 = :user")
  List<FriendShip> findAllByUser (@Param("user") User user);
}
