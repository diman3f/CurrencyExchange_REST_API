package org.pet.dao;


public interface DataAccessObject<T> {
    T getEntity(int id);
    T create();
}
