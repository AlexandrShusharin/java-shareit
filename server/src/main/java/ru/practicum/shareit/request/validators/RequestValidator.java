package ru.practicum.shareit.request.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

@Component
@RequiredArgsConstructor
public class RequestValidator {
    private final ItemRequestRepository itemRequestRepository;

    public void validateRequestIsExist(long requestId) {
        if (!itemRequestRepository.findById(requestId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Запрос с id = %s не существует", requestId));
        }
    }
}
