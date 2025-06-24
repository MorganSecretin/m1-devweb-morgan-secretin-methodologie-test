package fr.ynov.testdevweb.services;

import java.util.List;

import fr.ynov.testdevweb.dtos.UserDto;
import fr.ynov.testdevweb.entities.User;

public interface IUserService {
    List<UserDto> getAll();
    UserDto getById(Long id);
    UserDto create(User user);
    UserDto update(Long id, UserDto user);
    void delete(Long id);
}
