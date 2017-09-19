package com.dao;

import com.mongodb.DBObject;

import java.util.Collection;

/**
 * Created by vikasnaiyar on 18/09/17.
 */
public interface TagsDao<T,R> {

    R saveTag();

    R getTag(T id);

    Collection<R> getAllTags();

}
