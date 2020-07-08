package pl.wallet.transaction;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NotTransactionBack implements Specification<Transaction> {
  public NotTransactionBack (String transactionBack) {
  }

  @Override
  public Predicate toPredicate (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    return null;
  }
}
