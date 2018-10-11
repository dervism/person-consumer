package no.dervis.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;

@ExtendWith(PactConsumerTestExt.class)
class PersonServiceTest {

    private static final String PERSON_API_URL = "/api/person";
    private static final String CONSUMER = "person-consumer";
    private static final String PROVIDER = "person-provider";
    private static final Map<String, String> headers =
            Collections.singletonMap("Content-Type", "application/json;charset=utf-8");

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    public RequestResponsePact hasOnePerson(PactDslWithProvider builder) {

        return builder
                .given("a person exists")
                .uponReceiving("a request for a person that exist")
                    .matchPath("/api/person/\\d+")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(headers)
                    .body(buildJsonResponse())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "hasOnePerson")
    void verifyHasOnePerson(MockServer mockServer) throws IOException {
        PersonService personService = new PersonService(mockServer.getUrl());
        assertPersonExist(personService.getPerson(0));
    }

    private void assertPersonExist(Person person) {
        Assertions.assertNotNull(person);
        Assertions.assertEquals(0, person.getId());
        Assertions.assertEquals("Michael Johansen", person.getName());
        Assertions.assertEquals(50, person.getAge());
    }

    private DslPart buildJsonResponse() {
        return newJsonBody(body -> {
            body.numberType("id", 0);
            body.stringType("name", "Michael Johansen");
            body.numberType("age", 50);
        }).build();
    }

}