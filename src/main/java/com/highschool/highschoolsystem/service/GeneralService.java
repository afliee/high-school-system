package com.highschool.highschoolsystem.service;

import java.util.Optional;

public interface GeneralService<T> {
    T save(T t);
    T update(T t);
    Optional<T> findById(String id);
    void delete(String id);
    Iterable<T> findAll();
}
