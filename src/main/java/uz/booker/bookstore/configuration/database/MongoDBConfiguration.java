package uz.booker.bookstore.configuration.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "uz.booker.bookstore.repository.mongo")
public class MongoDBConfiguration {

    @Value("${mongo.db.url}")
    private String url;

    @Value("${mongo.db.username}")
    private String username;

    @Value("${mongo.db.password}")
    private String password;



    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(url);

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .credential(MongoCredential.createCredential(username, "bookstore", password.toCharArray()))
                .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, username);
    }


}
