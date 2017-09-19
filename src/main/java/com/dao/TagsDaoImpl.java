package com.dao;

import com.mongodb.*;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vikasnaiyar on 18/09/17.
 */
public class TagsDaoImpl implements TagsDao<String, DBObject> {

    private DBCollection collection = null;

    public TagsDaoImpl(String dbName, String collectionName) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB(dbName);
        collection = database.getCollection(collectionName);
    }

    @Override
    public DBObject saveTag() {
        DBObject resume = new BasicDBObject("name", "Vikas Naiyar")
                .append("email","vikasnaiyar@gmail.com");

        collection.insert(resume);
        //ObjectId id = (ObjectId)resume.get( "_id" );
        return resume;
    }

    @Override
    public DBObject getTag(String id) {
        DBObject query = new BasicDBObject("_id", new ObjectId(id));
        DBCursor cursor = collection.find(query);
        DBObject object = cursor.one();
        return object;
    }

    @Override
    public Collection<DBObject> getAllTags() {
        Collection<DBObject> objects = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            objects.add(cursor.next());
        }
        return objects;
    }
}
