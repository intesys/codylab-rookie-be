package it.intesys.codylab.rookie.mapper;

public interface Mapper<D, E> {
    E toEntity (D dto);
    D toDTO (E entity);
}
