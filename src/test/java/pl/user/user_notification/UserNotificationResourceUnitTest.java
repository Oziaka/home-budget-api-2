package pl.user.user_notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.user.UserDto;
import pl.user.UserProvider;
import pl.user.UserRandomTool;
import pl.user.user_notification.notification.*;
import pl.user.user_notification.notification.notification.NotificationProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class UserNotificationResourceUnitTest {

    private UserNotificationRepository userNotificationRepository;
    private UserProvider userProvider;
    private UserNotificationService userNotificationService;
    private NotificationProvider notificationProvider;
    private UserNotificationResource userNotificationResource;

    @BeforeEach
    void initResource() {
        userNotificationRepository = mock(UserNotificationRepository.class);
        userProvider = mock(UserProvider.class);
        notificationProvider = mock(NotificationProvider.class);
        userNotificationService = new UserNotificationService(userNotificationRepository, userProvider, notificationProvider);
        userNotificationResource = new UserNotificationResource(userNotificationService);
    }

    @Test
    void getNotificationsReturnUserNotifications() {
        // given
        UserDto userDto = UserRandomTool.randomUserDto();
        List<UserNotification> userNotifications = Stream.iterate(0, i -> i + 1).limit(10l).map(i -> UserNotificationRandomTool.randomUserNotification()).collect(Collectors.toList());
        // when
        when(userNotificationRepository.findAll((Specification<UserNotification>) any(), any())).thenReturn(userNotifications);
        ResponseEntity<List<UserNotificationDto>> userNotificationsResponseEntity = userNotificationResource.getNotifications(userDto::getEmail, null, (r, q, cb) -> null);
        // then
        List<UserNotificationDto> expectedUserNotifications = userNotifications.stream().map(UserNotificationMapper::toDto).collect(Collectors.toList());
        assertEquals(HttpStatus.OK, userNotificationsResponseEntity.getStatusCode());
        assertEquals(expectedUserNotifications, userNotificationsResponseEntity.getBody());
    }

    @Test
    void setUserNotificationStatusReturnUpdatedUserNotificationWhenIsUserNotification() {
        // given
        UserDto userDto = UserRandomTool.randomUserDto();
        UserNotification userNotification = UserNotificationRandomTool.randomUserNotification(1L);
        Status newStatus = UserNotificationRandomTool.randomUserNotificationStatus();
        // when
        when(userNotificationRepository.getByUserEmailAndAndId(any(), any())).thenReturn(Optional.of(userNotification));
        when(userNotificationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, UserNotification.class));
        ResponseEntity<UserNotificationDto> updatedUserNotificationResponseEntity = userNotificationResource.setUserNotificationStatus(userDto::getEmail, userNotification.getId(), newStatus);
        // then
        userNotification.setStatus(newStatus);
        UserNotificationDto expectedUpdatedUserNotification = UserNotificationMapper.toDto(userNotification);
        assertEquals(HttpStatus.OK, updatedUserNotificationResponseEntity.getStatusCode());
        assertEquals(expectedUpdatedUserNotification, updatedUserNotificationResponseEntity.getBody());
    }
}
