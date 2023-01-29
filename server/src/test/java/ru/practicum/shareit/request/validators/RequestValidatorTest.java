package ru.practicum.shareit.request.validators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestValidatorTest {
    @InjectMocks
    RequestValidator requestValidator;
    @Mock
    ItemRequestRepository requestRepository;
    private final long requestId = 1L;

    @Test
    void validateRequestIsExist() {
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> requestValidator.validateRequestIsExist(requestId));
        assertEquals(exception.getMessage(),
                String.format("Запрос с id = %s не существует", requestId));
    }
}