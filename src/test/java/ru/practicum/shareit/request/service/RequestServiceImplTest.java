package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.validators.RequestValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @InjectMocks
    private RequestServiceImpl requestService;
    @Mock
    private ItemRequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private RequestValidator requestValidator;

    private User user;
    private ItemRequest request;
    private ItemRequestResponseDto requestResponseDto;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("user")
                .email("user@mail.ru")
                .build();

        request = ItemRequest.builder()
                .id(1L)
                .description("need hummer")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        requestResponseDto = ItemRequestResponseDto.builder()
                .id(1L)
                .description("need hummer")
                .created(LocalDateTime.now())
                .requestor(user)
                .items(new ArrayList<>())
                .build();

        requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("need hummer")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void add() {
        doNothing().when(userValidator).validateUserIsExist(user.getId());
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(request);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        ItemRequestDto addedRequestDto = requestService.add(user.getId(), requestDto);
        assertThat(addedRequestDto.getId(), is(requestDto.getId()));
        assertThat(addedRequestDto.getRequestor().getId(), is(user.getId()));
        Mockito.verify(requestRepository, times(1)).save(any());
    }

    @Test
    void addWithWrongUser() {
        doThrow(new ObjectNotFoundException("404")).when(userValidator).validateUserIsExist(user.getId());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> requestService.add(user.getId(), requestDto));
        assertEquals("404", exception.getMessage());
    }

    @Test
    void get() {
        doNothing().when(userValidator).validateUserIsExist(anyLong());
        doNothing().when(requestValidator).validateRequestIsExist(anyLong());
        when(requestRepository.getReferenceById(anyLong())).thenReturn(request);
        ItemRequestResponseDto existRequestDto = requestService.get(user.getId(), request.getId());
        assertThat(existRequestDto.getId(), is(request.getId()));
        assertThat(existRequestDto.getRequestor().getId(), is(user.getId()));
    }

    @Test
    void getWrongId() {
        doThrow(new ObjectNotFoundException("404")).when(requestValidator).validateRequestIsExist(anyLong());
        final ObjectNotFoundException exception = Assertions.assertThrows(
                ObjectNotFoundException.class,
                () -> requestService.get(user.getId(), request.getId()));
        assertThat(exception.getMessage(), is("404"));
    }

    @Test
    void getAllByOwner() {
        doNothing().when(userValidator).validateUserIsExist(anyLong());
        when(requestRepository.findAllByRequestor_Id(user.getId())).thenReturn(List.of(request));
        List<ItemRequestResponseDto> requests = requestService.getAllByOwner(user.getId());
        assertThat(requests, hasSize(1));
        assertThat(requests.get(0).getId(), is(request.getId()));
    }

    @Test
    void getAll() {
        doNothing().when(userValidator).validateUserIsExist(anyLong());
        when(requestRepository.findAllByRequestor_IdNot(eq(user.getId()), any(Pageable.class))).thenReturn(List.of(request));
        List<ItemRequestResponseDto> requests = requestService.getAll(user.getId(), 0, 1);
        assertThat(requests, hasSize(1));
        assertThat(requests.get(0).getId(), is(request.getId()));
    }
}