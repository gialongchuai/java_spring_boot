package com.example.demo.service.impl;

import com.example.demo.dto.request.UserRequestDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<UserResponse> getAllUsers(int pageNo, int pageSize) {
        return List.of();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }
}
