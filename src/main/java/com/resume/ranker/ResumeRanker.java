package com.resume.ranker;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;

/**
 * Created by vikasnaiyar on 17/09/17.
 */
public class ResumeRanker {

    public static void main(String[] args) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        DB database = mongoClient.getDB("test");
        DBCollection collection = database.getCollection("resume");
        DBObject query = new BasicDBObject("_id", new ObjectId("59beb85177c8fa66181f1e2b"));
        DBCursor cursor = collection.find(query);
        DBObject jo = cursor.one();
        System.out.println(jo.get("name"));


       /* DBObject person = new BasicDBObject("name", "Vikas Naiyar")
                           .append("email","vikasnaiyar@gmail.com");

        WriteResult result = collection.insert(person);
        ObjectId id = (ObjectId)person.get( "_id" );
        System.out.println(id.toString());
*/
/*

        DBObject resume = new BasicDBObject()
                .append("personal", "Jo Bloggs")
                .append("address", new BasicDBObject("street", "123 Fake St")
                        .append("city", "Faketon")
                        .append("state", "MA")
                        .append("zip", 12345))
                .append("books", books);

        {
            "personal": {
            "name": "Vikas Naiyar",
                    "email": "vikasnaiyar@gmail.com",
                    "phone": "9902274569"
        },
            "education": {
            "colleges": [
            "IIT"
            ],
            "degrees": [
            "BTECH"
            ]
        },
            "skills": {
            "languages": [
            "Java",
                    "Python"
            ],
            "frameworks": [
            "Spring",
                    "Hibernate"
            ]
        },
            "experience": 120,
                "domain": [
            "Finance",
                    "Communication"
            ]
        }*/

    }

}
