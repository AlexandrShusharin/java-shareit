package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.util.Comparator;
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
        user = userRepository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        userValidator.validateUserIsExist(userId);
        User existUser = userRepository.getReferenceById(userId);

        if (userDto.getName() != null) {
            existUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(existUser.getEmail())) {
            existUser.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(existUser));
    }

    @Override
    public void delete(long id) {
        userValidator.validateUserIsExist(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDto get(long id) {
        userValidator.validateUserIsExist(id);
        return UserMapper.toUserDto(userRepository.getReferenceById(id));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
