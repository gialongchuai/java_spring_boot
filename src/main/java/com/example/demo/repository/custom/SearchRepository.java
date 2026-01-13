package com.example.demo.repository.custom;

import com.example.demo.dto.response.PageResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.custom.criteria.SearchCriteria;
import com.example.demo.repository.custom.criteria.UserSearchCriteriaQueryConsumer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getUserListOrderWithOneColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
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
        List<UserResponse> userResponses = getUsersIgnoreUserInAddresses(users);


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

    public PageResponse<?> advanceSearchUser(int pageNo, int pageSize, String sortBy, String street, String... search) {
        // firstName:T lastName:T => tạo object searchCriteria để hứng 3 group này

        // Thêm 3 thằng đó vào cái List
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        if (Objects.nonNull(search)) {
            for (String s : search) {
                if (StringUtils.hasLength(s)) {
                    Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)"); // : là like còn > < bên thằng consumer có lessthan hay greaterthan
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.find()) {
                        searchCriteria.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                    }
                }
            }
        }

        List<User> users = getUsers(pageNo, pageSize, searchCriteria, street, sortBy);
        List<UserResponse> userResponses = getUsersIgnoreUserInAddresses(users);

        Long totalElements = getTotalElements(searchCriteria);

        return PageResponse.builder()
                .pageNo(pageNo) // nếu chỉ định 1 thì id là 2 do ở dưới set cái criteria là Long và query nó
                .pageSize(pageSize) // giới hạn item có trong 1 lần request
                .totalElements(totalElements) // getSingle nên lấy được tổng totalEle
                .totalPages((int) Math.ceil(1.0 * totalElements / pageSize)) // này chia ra làm tròn lên để biết tổng số trang
                .items(userResponses)
                .build();
    }

    private Long getTotalElements(List<SearchCriteria> searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class); // Hình như đếm số totalEle dựa vào khóa chính của class User
        Root<User> root = query.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer consumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);
        searchCriteria.forEach(consumer);
        predicate = consumer.getPredicate();

        query.select(criteriaBuilder.count(root)); // Khác với truy vấn dòng này
        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult(); // và dòng này
    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> searchCriteria, String street, String sortBy) {
        // Nói chung ta có 1 list các searchCriteria chứa 3 group các điều kiện
        // Ta tạo criteria để duyệt qua từng cái trong list này trên đối tượng bên dưới User class xem thỏa
        // thì trả về response thế thôi !!!

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer consumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);
        searchCriteria.forEach(consumer);
        predicate = consumer.getPredicate();

        // kiểm tra bên dưới xem nếu có tồn tại thằng address thì nhét thêm cái addressPredice với street vào
        // còn nếu không thì nhảy vào else xét cái class user mà thôi !!!
        if (StringUtils.hasLength(street)) {
            Join<Address, User> addressUserJoin = root.join("addresses");
            Predicate addressPredicate = criteriaBuilder.like(addressUserJoin.get("street"), "%" + street + "%");
            query.where(predicate, addressPredicate);
        } else {
            query.where(predicate);
        }

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnName = matcher.group(1); // firstName
                if (matcher.group(3).equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
            }
        }

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }

    // tránh User trong adress kẻo vòng lặp vô tận
    public List<UserResponse> getUsersIgnoreUserInAddresses(List<User> users) {
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

        return userResponses;
    }
}
