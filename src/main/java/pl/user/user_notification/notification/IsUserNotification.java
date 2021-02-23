package pl.user.user_notification.notification;

import org.springframework.data.jpa.domain.Specification;
import pl.user.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class IsUserNotification implements Specification<UserNotification> {

  public IsUserNotification(User user) {
  }

  @Override
  public Predicate toPredicate(Root<UserNotification> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    return null;
  }
}
