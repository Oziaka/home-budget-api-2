package pl.wallet.transaction.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.user.User;
import pl.wallet.transaction.model.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class IsUserWallet implements Specification<Transaction> {
  public IsUserWallet(User user) {

  }

  @Override
  public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return null;
  }
}
