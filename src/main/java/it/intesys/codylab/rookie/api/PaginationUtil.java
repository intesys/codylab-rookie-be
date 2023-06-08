package it.intesys.codylab.rookie.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public class PaginationUtil {

    private PaginationUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable buildPageable(Integer page, Integer size, String sort) {
        if (sort != null && !sort.isBlank()) {
            Sort.Order order;
            String[] sortSplit = sort.split(",");
            String valueField = sortSplit[0];
            String sortingField = sortSplit[1];

            if (sortSplit.length == 2) {
                order = new Sort.Order(Sort.Direction.fromString(sortingField), valueField);
            } else {
                order = Sort.Order.by(sortSplit[0]);
            }

            return PageRequest.of(page, size, Sort.by(order));
        } else {
            return PageRequest.of(page, size);
        }
    }

    public static <T> HttpHeaders generatePaginationHttpHeaders(Page<T> page, String baseUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotalElements()));
        headers.add("X-Page-Number", Long.toString(page.getNumber()));
        headers.add("X-Page-Size", Long.toString(page.getNumberOfElements()));

        String link = "";
        if ((page.getNumber() + 1) < page.getTotalPages()) {
            link = "<" + generateUri(baseUrl, page.getNumber() + 1, page.getSize()) + ">; rel=\"next\",";
        }
        // prev link
        if ((page.getNumber()) > 0) {
            link += "<" + generateUri(baseUrl, page.getNumber() - 1, page.getSize()) + ">; rel=\"prev\",";
        }
        // last and first link
        int lastPage = 0;
        if (page.getTotalPages() > 0) {
            lastPage = page.getTotalPages() - 1;
        }
        link += "<" + generateUri(baseUrl, lastPage, page.getSize()) + ">; rel=\"last\",";
        link += "<" + generateUri(baseUrl, 0, page.getSize()) + ">; rel=\"first\"";
        headers.add(HttpHeaders.LINK, link);
        return headers;
    }

    private static String generateUri(String baseUrl, int page, int size) {
        return UriComponentsBuilder.fromUriString(baseUrl).queryParam("page", page).queryParam("size", size).toUriString();
    }
}
