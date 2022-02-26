package pl.wallet.transaction.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.exception.ThereIsNoYourPropertyException;
import pl.user.User;
import pl.user.UserService;
import pl.wallet.Wallet;
import pl.wallet.WalletService;
import pl.wallet.category.Category;
import pl.wallet.category.CategoryService;
import pl.wallet.transaction.dto.TransactionRecurringDto;
import pl.wallet.transaction.mapper.TransactionRecurringMapper;
import pl.wallet.transaction.model.TransactionRecurring;
import pl.wallet.transaction.repository.TransactionRecurringRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@EnableScheduling
public class TransactionRecurringService {

    private TransactionRecurringRepository transactionRecurringRepository;
    private UserService userService;
    private WalletService walletService;
    private CategoryService categoryService;
    private TransactionService transactionService;

    @Scheduled(fixedDelay = 60 * 1000)
    private void addTransactionAndCalculateTheNextAddition() {
        List<TransactionRecurring> transactionRecurringList = transactionRecurringRepository.findAll();
        transactionRecurringList.stream()
          .filter(transactionRecurring -> transactionRecurring.getDateOfNextAdding().isAfter(LocalDateTime.now()))
          .peek(transactionRecurring -> transactionService.save(transactionRecurring.getTransaction()))
          .peek(transactionRecurring -> transactionRecurring.setDateOfLastAdding(LocalDateTime.now()))
          .peek(transactionRecurring -> countNextAddition(transactionRecurring))
          .filter(transactionRecurring -> transactionRecurring != null)
          .forEach(transactionRecurringRepository::save);
    }

    private TransactionRecurring countNextAddition(TransactionRecurring transactionRecurring) {
        switch (transactionRecurring.getFrequency()) {
            case DAILY: {
                transactionRecurring.setDateOfNextAdding(LocalDateTime.now().plusDays(1));
                break;
            }
            case WEEKLY: {
                transactionRecurring.setDateOfNextAdding(LocalDateTime.now().plusDays(7));
                break;
            }
            case EVERY_YEAR: {
                transactionRecurring.setDateOfNextAdding(LocalDateTime.now().plusYears(1));
                break;
            }
            case MONTHLY: {
                transactionRecurring.setDateOfNextAdding(LocalDateTime.now().plusMonths(1));
                break;
            }
            case CUSTOM_REPETITION: {
                transactionRecurring.setDateOfNextAdding(LocalDateTime.now().plusDays(transactionRecurring.getDay()));
                break;
            }
        }
        if (transactionRecurring.getEnd().compareTo(transactionRecurring.getDateOfNextAdding()) < 0) {
            remove(transactionRecurring);
            return null;
        }
        if (transactionRecurring.getNumberOfRepetition() == 1) {
            remove(transactionRecurring);
            return null;
        }
        return transactionRecurring;
    }

    public TransactionRecurringDto addTransactionRecurring(Principal principal, Long walletId, TransactionRecurringDto transactionRecurringDto) {
        User user = userService.getUser(principal);
        Wallet wallet = walletService.isUserWallet(principal.getName(), walletId);
        Category category = categoryService.getCategory(user, transactionRecurringDto.getTransaction().getCategoryId());
        TransactionRecurring transactionRecurring = TransactionRecurringMapper.toEntity(transactionRecurringDto, category);
        TransactionRecurring savedTransactionRecurring = this.save(transactionRecurring);
        wallet.addTransactionRecurring(transactionRecurring);
        walletService.save(wallet);
        return TransactionRecurringMapper.toDto(savedTransactionRecurring);
    }


    public void removeTransactionRecurring(Principal principal, Long walletId, Long transactionRecurringId) {
        this.remove(this.getOne(principal.getName(), walletId, transactionRecurringId));
    }

    public TransactionRecurring save(TransactionRecurring transactionRecurring) {
        return transactionRecurringRepository.save(transactionRecurring);
    }

    private TransactionRecurring getOne(String email, Long walletId, Long transactionRecurringId) {
        return transactionRecurringRepository.get(email, walletId, transactionRecurringId).orElseThrow(ThereIsNoYourPropertyException::new);
    }

    public void remove(TransactionRecurring transactionRecurring) {
        transactionRecurringRepository.delete(transactionRecurring);
    }


}
