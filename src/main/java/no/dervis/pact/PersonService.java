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
        String content = Request.Get(host + endpoint + "/" + id)
                .execute()
                .returnContent().asString();

        return new ObjectMapper().readValue(content, Person.class);
    }

    public int createPerson(String name, int age) throws IOException {
        String string = new ObjectMapper()
                .writeValueAsString(new Person(name, age));

        return Request.Post(host + endpoint).bodyString(
                string, ContentType.APPLICATION_JSON)
                .execute().returnResponse().getStatusLine().getStatusCode();
    }

}
