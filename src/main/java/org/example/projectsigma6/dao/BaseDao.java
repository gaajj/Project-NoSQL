package org.example.projectsigma6.dao;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.example.projectsigma6.codecs.EmployeeCodec;

public abstract class BaseDao<T> {

    protected final MongoDatabase database;
    protected final MongoCollection<T> collection;

    public BaseDao(MongoClient mongoClient, String databaseName, String collectionName, Class<T> clazz) {
        // Setup codec registry
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new EmployeeCodec())
        );
        // Initialize using codec registry
        this.database = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
        this.collection = database.getCollection(collectionName, clazz);
    }

}
