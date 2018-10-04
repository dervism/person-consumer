package no.dervis.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;

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

    public Person getPersonFromQueryParam(int id) throws IOException {
        Person person = new ObjectMapper().readValue(Request.Get(host + endpoint + "?fnr=" + id).execute()
                .returnContent().asString(), Person.class);

        return person;
    }

}
