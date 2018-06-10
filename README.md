# Person Consumer app

Example app showing how to write a Consumer-contract using JUnit 5 and how to upload it using the pact maven plugin.

Update the pact-broker url in the pom file (pactBrokerUrl property), then run

    mvn clean install pact:publish
    
