package com.example.demo.service.impl;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.UserRequestDTO;
import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.custom.SearchRepository;
import com.example.demo.repository.custom.specification.UserSpecificationBuilder;
import com.example.demo.service.UserService;
import com.example.demo.util.UserStatus;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    SearchRepository searchRepository;
    EmailServiceImpl emailService;
    PasswordEncoder passwordEncoder;

//    KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Long saveUser(UserRequestDTO requestDTO) {
        if(userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new ResourceNotFoundException("username is existed!");
        }

        User user = User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .dateOfBirth(requestDTO.getDateOfBirth())
                .gender(requestDTO.getGender())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode((requestDTO.getPassword())))
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

//        // === Cách 01 === Truyền thẳng qua send String bình thường : Tầm 4 5 s
//        try {
//            emailService.sendWelcomeEmail(userRes.getEmail());
//        } catch (MessagingException e) {
//            //   Xử lý lỗi gửi mail (log, retry, v.v.)
//            throw new RuntimeException("Gửi email thất bại", e);
//        }

//        // === Cách 02 === Kafka gửi topic : Truyền thông qua lắng nghe topic tốn khoản 800 ms
//        if (userRes != null) {
//            String messages = String.format("email:%s,id:%s,code:%s"
//                    , userRes.getEmail(), user.getId(), "code@123");
//            kafkaTemplate.send("confirm-account-topic", messages);
//        }

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
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
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
    public PageResponse<?> getAllUsers(int pageNo, int pageSize, String sortBy) {
        int page = pageNo; // gọi 0 1 cũng ra trang đầu
        if (pageNo > 0) {
            page--;
        }

        List<Sort.Order> orders = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) { // thỏa sort theo tiêu chí firstName:asc hoặc lastName:desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); // group 1 là cái gì cũng dc
            Matcher matcher = pattern.matcher(sortBy); // gr 2 bắt buộc là :
            if (matcher.find()) { // nếu thỏa           // gr 3 là gì cũng dc dưới kiểm tra phải là asc or desc
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
    public PageResponse<?> getAllUsersOrderWithMultipleColumns(int pageNo, int pageSize, String... sorts) {
        int page = pageNo; // gọi 0 1 cũng ra trang đầu
        if (pageNo > 0) {
            page--;
        }
        List<Sort.Order> orders = new ArrayList<>();
        if (Objects.nonNull(sorts)) {
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

    @Override
    public PageResponse<?> getUserListOrderWithOneColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        return searchRepository.getUserListOrderWithOneColumnAndSearch(pageNo, pageSize, search, sortBy);
    }

    @Override
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String street, String... search) {
        return searchRepository.advanceSearchUser(pageNo, pageSize, sortBy, street, search);
    }

    @Override
    public PageResponse<?> advanceSearchWithSpecification(Pageable pageable, String[] user, String[] address) {

        if (user != null && address != null) { // join table để tìm kiếm
            return searchRepository.getUserJoinedAddress(pageable, user, address);
        } else if (user != null) { // có đúng add null nên ko cần join tìm đúng user

//            Specification<User> spec = UserSpec.hasFirstName("T");
//            Specification<User> genderSpec = UserSpec.notEqualGender(Gender.MALE);
//            Specification<User> finalSpec = spec.and(genderSpec);

            UserSpecificationBuilder builder = new UserSpecificationBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)");
            for (String u : user) {
                Matcher matcher = pattern.matcher(u);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2)
                            , matcher.group(3), matcher.group(4)
                            , matcher.group(5));

                }
            }

            Page<User> users = userRepository.findAll(builder.build(), pageable);
            List<User> listUser = users.stream().toList();
            List<UserResponse> userResponses = SearchRepository.getUsersIgnoreUserInAddresses(listUser);

            long totalElements = users.getTotalElements();

            return PageResponse.builder()
                    .pageNo(pageable.getPageNumber())
                    .pageSize(pageable.getPageSize())
                    .totalElements(totalElements)
                    .totalPages((int) Math.ceil(1.0 * totalElements / pageable.getPageSize()))
                    .items(userResponses)
                    .build();
        }

        Page<User> users = userRepository.findAll(pageable);
        List<User> listUser = users.stream().toList();
        List<UserResponse> userResponses = SearchRepository.getUsersIgnoreUserInAddresses(listUser);

        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .items(userResponses)
                .build();
    }

    @Override
    public PageResponse<?> testingApiFilterUserAndAddress(String lastName, String street) {
        List<User> users = userRepository.findAllByLastNameAndStreet(lastName, street);
        List<UserResponse> userResponses = SearchRepository.getUsersIgnoreUserInAddresses(users);
        return PageResponse.builder()
                .items(userResponses)
                .build();
    }


    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(Translator.toLocale("user.not.found")));
    }
}
