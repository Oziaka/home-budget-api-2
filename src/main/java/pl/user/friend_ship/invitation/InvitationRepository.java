package pl.user.friend_ship.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findAllByInviterAndInvited(User inviter, User invited);

    void removeByInviterAndInvited(User inviter, User Invited);

    void delete(Invitation invitation);

    Optional<Invitation> findByInviterAndId(User inviter, Long invitationId);

    Optional<Invitation> findByInvitedAndId(User invited, Long invitationId);

    List<Invitation> findAllByInviter(User inviter);

    List<Invitation> findAllByInvited(User invited);
}

