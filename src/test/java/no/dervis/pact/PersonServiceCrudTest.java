package no.dervis.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static io.pactfoundation.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PactConsumerTestExt.class)
class PersonServiceCrudTest {

    private static final String CONSUMER = "person-consumer";
    private static final String PROVIDER = "person-provider";
    private static final Map<String, String> headers =
            Collections.singletonMap("Content-Type", "application/json;charset=utf-8");

    @Pact(consumer = CONSUMER, provider = PROVIDER)
    public RequestResponsePact canCreatePerson(PactDslWithProvider builder) {

        return builder
                .given("a person can be created")
                .uponReceiving("a request to create a person")
                    .path("/api/person")
                    .method("POST")
                    .body(newJsonBody(body -> {
                        body.numberType("id", 0);
                        body.stringType("name", "Michael Johansen");
                        body.numberType("age", 50);
                    }).build())
                .willRespondWith()
                    .status(201)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "canCreatePerson")
    void verifyHasOnePerson(MockServer mockServer) throws IOException {
        PersonService personService = new PersonService(mockServer.getUrl());
        boolean created = personService.createPerson("foo bar", 36);

        assertTrue(created);
    }

}