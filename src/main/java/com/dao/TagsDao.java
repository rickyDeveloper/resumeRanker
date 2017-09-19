package com.dao;

import com.mongodb.DBObject;

import java.util.Collection;
import java.util.Map;

/**
 * Created by vikasnaiyar on 18/09/17.
 */
public interface TagsDao<T,R> {

    R saveTag();

    R saveTag(Map json);
    
    R getTag(T id);

    Collection<R> getAllTags();

}
