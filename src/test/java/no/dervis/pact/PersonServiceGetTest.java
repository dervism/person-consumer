package no.dervis.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static java.util.Collections.singletonMap;

@ExtendWith(PactConsumerTestExt.class)
class PersonServiceGetTest {

    private static final String CONSUMER = "person-consumer";
    private static final String PROVIDER = "person-provider";

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    public RequestResponsePact hasOnePerson(PactDslWithProvider builder) {

        return builder
                .given("one person exists")
                .uponReceiving("a request for a person that exist")
                    .path("/api/person/0")                //.matchPath("/api/person/[0-9]+")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .headers(singletonMap("Content-Type", "application/json;charset=utf-8"))
                    .body(newJsonBody(body -> {
                        body.numberType("id", 0);
                        body.stringType("name", "Michael Johansen");
                        body.numberType("age", 50);
                    }).build())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "hasOnePerson")
    void verifyHasOnePerson(MockServer mockServer) throws IOException {
        PersonService personService = new PersonService(mockServer.getUrl());

        Person person = personService.getPerson(0);

        Assertions.assertNotNull(person);
        Assertions.assertEquals(0, person.getId());
        Assertions.assertEquals("Michael Johansen", person.getName());
        Assertions.assertEquals(50, person.getAge());
    }

}