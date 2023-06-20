package org.example.patientrepository;


    import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

    public class RookieRepository {
        public static final int LATEST_RECORD_SIZE = 5;

        protected String page(StringBuilder buffer, Pageable pageable) {
            Sort sort = pageable.getSort();
            buffer.append(' ');
            if (!sort.isEmpty()) {
                buffer.append("order by ");
                sort.stream()
                        .forEach(order -> {
                            String property = order.getProperty();
                            Sort.Direction direction = order.getDirection();
                            buffer.append(property);
                            if (direction == Sort.Direction.DESC)
                                buffer.append(' ').append("desc").append(' ');
                        });
            }
            int limit = pageable.getPageSize();
            long offset = pageable.getOffset();
            buffer.append("limit").append(' ').append(limit).append(' ')
                    .append("offset").append(' ').append(offset).append(' ');
            return buffer.toString();
        }
    }

