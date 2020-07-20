package pl.user.user_notification.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wallet.transaction.Transaction;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/notification")
public class UserNotificationResource {

    private UserNotificationController userNotificationController;

    @GetMapping("/notification")
    public ResponseEntity<List<UserNotificationDto>> getNotifications(Principal principal,
                                                                      @PageableDefault(page = 0, size = 40)
                                                                      @SortDefault.SortDefaults(
                                                                              @SortDefault(sort = "notification.dateOfAdding", direction = Sort.Direction.DESC))
                                                                              Pageable pageable,
                                                                      @Spec(path = "status", params = "status", spec = In.class)
                                                                              Specification<UserNotification> userNotificationSpecification) {
        return ResponseEntity.ok(userNotificationController.getNotifications(principal, pageable, userNotificationSpecification));
    }

    @PostMapping("/change_status/{userNotificationId}")
    public ResponseEntity<UserNotificationDto> setUserNotificationStatus(Principal principal, @PathVariable Long userNotificationId, @RequestBody Status newStatus) {
        return ResponseEntity.ok(userNotificationController.updateStatus(principal, userNotificationId, newStatus));
    }
}
