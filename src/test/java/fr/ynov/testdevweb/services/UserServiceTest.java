package fr.ynov.testdevweb.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.ynov.testdevweb.dtos.UserDto;
import fr.ynov.testdevweb.entities.User;
import fr.ynov.testdevweb.exceptions.EmailAlreadyExistsException;
import fr.ynov.testdevweb.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;
    private User user;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        user = new User("John Doe", "john@example.com", "passwordOFJohn56");
        user.setId(1L);
    }

    // 1. Test de récupération de tous les utilisateurs
    @Test
    void testGetAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("john@example.com", result.get(0).getEmail());
    }

    // 2. Test de récupération d’un utilisateur par ID (trouvé)
    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getById(1L);

        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    // 3. Test de récupération d’un utilisateur par ID (non trouvé)
    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getById(1L));
    }

    // 4. Test de création d’un utilisateur (succès)
    @Test
    void testCreateUser_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.create(user);

        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    // 5. Test de création d’un utilisateur avec email existant
    @Test
    void testCreateUser_EmailExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(user));
    }

    // 6. Test de mise à jour d’un utilisateur (succès)
    @Test
    void testUpdateUser_Success() {
        UserDto updatedDto = new UserDto(1L, "Updated", "updated@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserDto result = userService.update(1L, updatedDto);

        assertEquals("Updated", result.getName());
        assertEquals("updated@example.com", result.getEmail());
    }

    // 7. Test de mise à jour d’un utilisateur (non trouvé)
    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserDto updatedDto = new UserDto(1L, "Updated", "updated@example.com");

        assertThrows(EntityNotFoundException.class, () -> userService.update(1L, updatedDto));
    }

    // 8. BONUS - Test mise à jour avec un email déjà utilisé par un autre utilisateur
    @Test
    void testUpdateUser_EmailAlreadyExists() {
        User existingUser = new User("Existing", "existing@example.com", "existingpass");
        existingUser.setId(2L);

        UserDto updatedDto = new UserDto(1L, "Updated", "existing@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User toSave = inv.getArgument(0);
            if (toSave.getEmail().equals("existing@example.com") && !toSave.getId().equals(existingUser.getId())) {
                throw new EmailAlreadyExistsException("Email already exists: existing@example.com");
            }
            return toSave;
        });

        assertThrows(EmailAlreadyExistsException.class, () -> userService.update(1L, updatedDto));
    }

    // 9. Test de suppression d’un utilisateur (succès)
    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository, times(1)).delete(user);
    }

    // 10. Test de suppression d’un utilisateur (non trouvé)
    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.delete(1L));
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }
}
