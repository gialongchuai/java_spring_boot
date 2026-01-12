package com.example.demo.repository.custom;

import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getUserListOrderWithMultipleColumnsAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        StringBuilder sqlQuery = new StringBuilder("select u from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:lastName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");
        }

        if (StringUtils.hasLength(sortBy)) { // xử lý cho order 1 cái thôi nhiều quá gãy !!!!!
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                sqlQuery.append(String.format(" order by %s %s", matcher.group(1), matcher.group(3)));
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo); // current page
        selectQuery.setMaxResults(pageSize); // max record
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter("firstName", "%" + search + "%"); // hoặc String.format("%%%s%%", search)
            selectQuery.setParameter("lastName", "%" + search + "%");
            selectQuery.setParameter("email", "%" + search + "%");
        }
        List<User> users = selectQuery.getResultList();

        // map để bỏ cái user trong address kẻo output không hồi kết ;)))
        List<UserResponse> userResponses = users.stream().map(user -> {
            UserResponse userResponse = UserResponse.builder()
                    .id(user.getId())
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
                    .build();
            if (user.getAddresses() != null) {
                Set<Address> cleanAddress = user.getAddresses().stream()
                        .map(address -> {
                            return Address.builder()
                                    .apartmentNumber(address.getApartmentNumber())
                                    .floor(address.getFloor())
                                    .building(address.getBuilding())
                                    .streetNumber(address.getStreetNumber())
                                    .street(address.getStreet())
                                    .city(address.getCity())
                                    .country(address.getCountry())
                                    .addressType(address.getAddressType())
                                    .build();
                        }).collect(Collectors.toSet());
                userResponse.setAddresses(cleanAddress);
            } else {
                userResponse.setAddresses(new HashSet<>());
            }

            return userResponse;
        }).toList();


//        // count records (ele)
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" or lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" or lower(u.email) like lower(?3)");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
//        selectCountQuery.setFirstResult(pageNo); // current page
//        selectCountQuery.setMaxResults(pageSize); // max record
        if (StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
            selectCountQuery.setParameter(3, String.format("%%%s%%", search));
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil(1.0 * totalElements / pageSize))
                .items(userResponses)
                .build();
    }
}
