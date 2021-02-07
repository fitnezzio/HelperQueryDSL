package io.fitnezz.helper.querydsl.filter;

import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class GenericPageableFilter<T> extends GenericFilter<T> {

    private int page = 0;
    private int size = 10;
    private Sort sort;

    public GenericPageableFilter(EntityPathBase<T> entityPathBase, Integer page, Integer size, String direction, String sort) {
        super(entityPathBase);

        this.setPage(page);
        this.setSize(size);
        this.setSort(direction, sort);
    }

    public void setPage(Integer page) {
        if (page != null) {
            this.page = page - 1;
        }
    }

    public void setSize(Integer size) {
        if (size != null) {
            this.size = size;
        }
    }

    public void setSort(String direction, String fieldSort) {
        if (!Objects.isNull(fieldSort)) {
            Sort.Order order = createSortOrder(direction, fieldSort);
            this.sort = Sort.by(order);
        }
    }

    public void setSortIgnoreCase(String direction, String fieldSort) {
        if (!Objects.isNull(fieldSort)) {
            Sort.Order order = createSortOrder(direction, fieldSort);
            this.sort = Sort.by(order.ignoreCase());
        }
    }

    public PageRequest getPageRequest() {
        return this.sort == null ? PageRequest.of(this.page, this.size) : PageRequest.of(this.page, this.size, this.sort);
    }

    private static Sort.Order createSortOrder(String directionStr, String fieldSort) {
        Sort.Direction direction = getSortDirectionFromString(directionStr);
        return new Sort.Order(direction, fieldSort);
    }

    private static Sort.Direction getSortDirectionFromString(String directionStr) {
        return Objects.isNull(directionStr) ? Sort.Direction.ASC : Sort.Direction.fromString(directionStr);
    }

}
