package rookie.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class RookieAPI {
    protected Pageable pageable(Integer pageIndex, Integer size, String sort) {
        if (sort != null && !sort.isBlank()) {
            Sort.Order order;
            String[] sortSplit = sort.split(",");
            String field = sortSplit[0];
            String direction = sortSplit[1];

            if (sortSplit.length == 2) {
                order = new Sort.Order(Sort.Direction.fromString(direction), field);
            } else {
                order = Sort.Order.by(field);
            }

            return PageRequest.of(pageIndex, size, Sort.by(order));
        } else {
            return PageRequest.of(pageIndex, size);
        }
    }
}
