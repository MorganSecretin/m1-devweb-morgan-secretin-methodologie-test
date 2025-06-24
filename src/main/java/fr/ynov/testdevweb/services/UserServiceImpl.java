package fr.ynov.testdevweb.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.ynov.testdevweb.dtos.UserDto;
import fr.ynov.testdevweb.entities.User;
import fr.ynov.testdevweb.exceptions.EmailAlreadyExistsException;
import fr.ynov.testdevweb.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
            .map(this::toDto)
            .toList();
    }

    @Override
    public UserDto getById(Long id) {
        User user = getFullUserById(id);
        return toDto(user);
    }

    private User getFullUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDto create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists: " + user.getEmail());
        }
        User userSaved = userRepository.save(user);
        return toDto(userSaved);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User existing = getFullUserById(id);
        // On conserve le mot de passe existant
        User updatedUser = toEntity(userDto, existing.getPassword());
        updatedUser.setId(existing.getId());
        User userSaved = userRepository.save(updatedUser);
        return toDto(userSaved);
    }

    @Override
    public void delete(Long id) {
        User existing = getFullUserById(id);
        userRepository.delete(existing);
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    private User toEntity(UserDto dto, String password) {
        return new User(dto.getName(), dto.getEmail(), password);
    }
}
