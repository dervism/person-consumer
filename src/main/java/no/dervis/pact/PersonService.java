package no.dervis.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;

public class PersonService {

    private String host;
    private String endpoint = "/api/person";

    public PersonService(String host) {
        this.host = host;
    }

    public Person getPerson(int id) throws IOException {
        Person person = new ObjectMapper().readValue(Request.Get(host + endpoint + "/" + id).execute()
                .returnContent().asString(), Person.class);

        return person;
    }

    public boolean createPerson(String name, int age) throws IOException {
        int statusCode = Request.Post(host + endpoint).bodyString(
                new ObjectMapper()
                        .writeValueAsString(new Person(name, age)), ContentType.APPLICATION_JSON)
                .execute().returnResponse().getStatusLine().getStatusCode();

        return statusCode == 201;
    }

}
