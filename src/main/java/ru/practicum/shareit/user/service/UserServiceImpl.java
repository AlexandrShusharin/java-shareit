package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public UserDto add(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userValidator.validateEmailNotOccupied(user.getEmail());
        user = userRepository.add(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        userValidator.validateUserIsExist(userId);
        User existUser = userRepository.get(userId);

        if (userDto.getName() != null) {
            existUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(existUser.getEmail())) {
            userValidator.validateEmailNotOccupied(userDto.getEmail());
            existUser.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(userRepository.update(existUser));
    }

    @Override
    public void delete(long id) {
        userValidator.validateUserIsExist(id);
        userRepository.delete(id);
    }

    @Override
    public UserDto get(long id) {
        userValidator.validateUserIsExist(id);
        return UserMapper.toUserDto(userRepository.get(id));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
