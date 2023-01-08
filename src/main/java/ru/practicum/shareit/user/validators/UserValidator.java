package ru.practicum.shareit.user.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeptions.ObjectNotFoundException;
import ru.practicum.shareit.exeptions.UserEmailOccupiedException;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateEmailNotOccupied(String email) {
        if (userRepository.findAll().stream()
                .anyMatch(o -> o.getEmail().equals(email))) {
            throw new UserEmailOccupiedException(String.format("Email = %s уже занят", email));
        }
    }

    public void validateUserIsExist(long userId) {
        if (userRepository.getReferenceById(userId) == null) {
            throw new ObjectNotFoundException(String.format("Пользователь с id = %s не существует", userId));
        }
    }

}
