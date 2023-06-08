package it.intesys.codylab.rookie.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RookieRepository {
    @Autowired
    JdbcTemplate db;

    protected String pagingQuery(Pageable pageable, StringBuilder query) {
        String orderSep = "";
        Sort sort = pageable.getSort();
        if (!sort.isEmpty()) {
            query.append(" order by ");
            for (Sort.Order order: sort) {
                query.append(orderSep)
                        .append(order.getProperty())
                        .append(' ')
                        .append(order.getDirection().isDescending() ? "desc" : "")
                        .append(' ');
                orderSep = ", ";
            }
        }

        query.append("limit ")
                .append(pageable.getPageSize())
                .append(' ')
                .append("offset ")
                .append(pageable.getOffset());

        return query.toString();
    }

    protected <T> List<T> subtract(List<T> a, List<T> b) {
        List<T> insertions = new ArrayList<>(a);
        insertions.removeAll(b);
        return insertions;
    }

    protected <T> List<T> intersect(List<T> a, List<T> b) {
        return a.stream()
            .filter(b::contains)
            .toList();
    }

}
