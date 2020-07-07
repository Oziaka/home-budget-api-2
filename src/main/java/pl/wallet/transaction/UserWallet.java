package pl.wallet.transaction;

import org.springframework.data.jpa.domain.Specification;
import pl.user.User;
import pl.wallet.transaction.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class UserWallet implements Specification<Transaction> {
  public UserWallet (User user) {
  }

  @Override
  public Predicate toPredicate (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return null;
  }
}
