package pl.user.item_key;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserItemKeyRepository extends JpaRepository<UserItemKey, Long> {
}
