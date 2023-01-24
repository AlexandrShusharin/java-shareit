package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.validators.RequestValidator;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validators.UserValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final RequestValidator requestValidator;

    @Override
    public ItemRequestDto add(long userId, ItemRequestDto itemRequestDto) {
        userValidator.validateUserIsExist(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(userRepository.getReferenceById(userId));
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestResponseDto get(long userId, long requestId) {
        userValidator.validateUserIsExist(userId);
        requestValidator.validateRequestIsExist(requestId);
        return this.itemRequestToReponseDto(itemRequestRepository.getReferenceById(requestId));
    }

    @Override
    public List<ItemRequestResponseDto> getAllByOwner(long userId) {
        userValidator.validateUserIsExist(userId);
        return new ArrayList<>(itemRequestRepository.findAllByRequestor_Id(userId).stream()
                .map(this::itemRequestToReponseDto)
                .collect(Collectors.toList()));
    }

    @Override
    public List<ItemRequestResponseDto> getAll(long userId, int from, int size) {
        userValidator.validateUserIsExist(userId);
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sortById);
        return new ArrayList<>(itemRequestRepository.findAll(page).stream()
                .map(this::itemRequestToReponseDto)
                .collect(Collectors.toList()));
    }

    private ItemRequestResponseDto itemRequestToReponseDto(ItemRequest itemRequest) {
        List<Item> items = itemRepository.findAllByRequest_Id(itemRequest.getId());

        return ItemRequestResponseDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .items(new ArrayList<>(itemRepository.findAllByRequest_Id(itemRequest.getId()).stream()
                        .map(ItemMapper::toItemRequestDto)
                        .collect(Collectors.toList())))
                .build();
    }
}
