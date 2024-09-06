package com.example.Book.Store.Application.mapper;

public interface Mapper<T, U> {
    T mapToEntity(U dto);
    U mapToDTO(T entity);
}
