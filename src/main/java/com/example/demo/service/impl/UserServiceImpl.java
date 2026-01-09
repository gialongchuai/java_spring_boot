package com.example.demo.service.impl;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.UserStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;

    @Override
    public Long saveUser(UserRequestDTO requestDTO) {
        User user = User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .dateOfBirth(requestDTO.getDateOfBirth())
                .gender(requestDTO.getGender())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(requestDTO.getPassword())
                .status(requestDTO.getStatus())
                .type(requestDTO.getType())
//                .addresses(convertToAddress(requestDTO.getAddresses()))
                .build();
        requestDTO.getAddresses().forEach(address -> {
            user.saveAddress(Address.builder()
                    .apartmentNumber(address.getApartmentNumber())
                    .floor(address.getFloor())
                    .building(address.getBuilding())
                    .streetNumber(address.getStreetNumber())
                    .street(address.getStreet())
                    .city(address.getCity())
                    .country(address.getCountry())
                    .addressType(address.getAddressType())
                    .build());
        });
        User userRes = userRepository.save((user));
        log.info("User added successfully!");
        return userRes.getId();
    }

    private Set<Address> convertToAddress(Set<Address> addresses) {
        Set<Address> result = new HashSet<>();
        addresses.forEach(address -> {
            result.add(Address.builder()
                    .apartmentNumber(address.getApartmentNumber())
                    .floor(address.getFloor())
                    .building(address.getBuilding())
                    .streetNumber(address.getStreetNumber())
                    .street(address.getStreet())
                    .city(address.getCity())
                    .country(address.getCountry())
                    .addressType(address.getAddressType())
                    .build());
        });
        return result;
    }

    @Override
    public void updateUser(Long userId, UserRequestDTO requestDTO) {
        User user = getUserById(userId);
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setDateOfBirth(requestDTO.getDateOfBirth());
        user.setGender(requestDTO.getGender());
        user.setPhone(requestDTO.getPhone());
        user.setEmail(requestDTO.getEmail());
        user.setUsername(requestDTO.getUsername());
        user.setPassword(requestDTO.getPassword());
        user.setStatus(requestDTO.getStatus());
        user.setType(requestDTO.getType());
        user.setAddresses(convertToAddress(requestDTO.getAddresses()));

//        requestDTO.getAddresses().forEach(address -> {
//            user.saveAddress(Address.builder()
//                    .apartmentNumber(address.getApartmentNumber())
//                    .floor(address.getFloor())
//                    .building(address.getBuilding())
//                    .streetNumber(address.getStreetNumber())
//                    .street(address.getStreet())
//                    .city(address.getCity())
//                    .country(address.getCountry())
//                    .addressType(address.getAddressType())
//                    .build());
//        });

        userRepository.save(user);
        log.info("Updated user successfully!");
    }

    @Override
    public void changeStatusUser(Long userId, UserStatus userStatus) {
        User user = getUserById(userId);
        user.setStatus(userStatus);
        userRepository.save(user);
        log.info("Changed status user successfully!");
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("Deleted user successfully!");
    }

    @Override
    public UserResponse getUser(Long userId) {
        User user = getUserById(userId);
        UserResponse userResponse = UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .phone(user.getPhone())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .status(user.getStatus())
                .type(user.getType())
                .addresses(convertToAddress(user.getAddresses()))
                .build();
        return userResponse;
    }

    @Override
    public PageResponse getAllUsers(int pageNo, int pageSize, String sortBy) {
        int page = pageNo; // gọi 0 1 cũng ra trang đầu
        if (pageNo > 0) {
            page--;
        }

        List<Sort.Order> orders = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) { // thỏa sort theo tiêu chí firstName:asc hoặc lastName:desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) { // nếu thỏa
                // tìm kiểu trong regex với cái group 3: (.*)
                // TĂNG:firstName || GIẢM:lastName => ví dụ đại khái vậy
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = StringUtils.hasLength(sortBy) ? PageRequest.of(page, pageSize, Sort.by(orders)) : PageRequest.of(page, pageSize);
        Page<User> users = userRepository.findAll(pageable);

        List<UserResponse> userResponses = users.stream().map(user -> {
            return UserResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .dateOfBirth(user.getDateOfBirth())
                    .gender(user.getGender())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .type(user.getType())
                    .status(user.getStatus())
                    //                    .addresses(user.getAddresses())
                    .build();
        }).toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .items(userResponses)
                .build();
    }

    @Override
    public PageResponse getAllUsersOrderWithMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int page = pageNo; // gọi 0 1 cũng ra trang đầu
        if (pageNo > 0) {
            page--;
        }
        List<Sort.Order> orders = new ArrayList<>();
        if(Objects.nonNull(sorts)) {
            for (String sort : sorts) {
                if (StringUtils.hasLength(sort)) {
                    Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                    Matcher matcher = pattern.matcher(sort);
                    if (matcher.find()) { // nếu thỏa
                        // tìm kiểu trong regex với cái group 3: (.*)
                        // TĂNG:firstName || GIẢM:lastName => ví dụ đại khái vậy
                        if (matcher.group(3).equalsIgnoreCase("asc")) {
                            orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                        } else if (matcher.group(3).equalsIgnoreCase("desc")) {
                            orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                        }
                    }
                }
            }
        }

        Pageable pageable = Objects.isNull(sorts) ? PageRequest.of(page, pageSize) : PageRequest.of(page, pageSize, Sort.by(orders));
        Page<User> users = userRepository.findAll(pageable);

        List<UserResponse> userResponses = users.stream().map(user -> {
            return UserResponse.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .dateOfBirth(user.getDateOfBirth())
                    .gender(user.getGender())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .type(user.getType())
                    .status(user.getStatus())
//                    .addresses(user.getAddresses())
                    .build();
        }).toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .items(userResponses)
                .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("user.not.found")));
    }
}
