package pl.user.friend_ship.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findAllByInviterAndInvited(User inviter, User invited);

    void removeByInviterAndInvited(User inviter, User Invited);

    Optional<Invitation> findByKey(String key);

    void delete(Invitation invitation);
}

