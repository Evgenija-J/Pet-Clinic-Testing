

# Pet Clinic Application Testing with Spock and Groovy

This project is focused on testing an open-source Pet Clinic software using Spock and Groovy. The Pet Clinic app, originally developed in **Java Spring Boot**, allows users to manage owners, pets, visits, and vets. You can find the original repository [here](https://github.com/spring-projects/spring-petclinic/tree/main).

We implemented various test levels, including **Unit**, **Integration**, and **End-to-End (E2E)** tests, to ensure the functionality of the application is thoroughly validated.

---

## Test Types

### Unit Tests:
- **Model Creation/Editing/Deletion**: Direct tests on model objects to ensure CRUD operations function as expected.
- **Model Methods**: Testing individual methods that interact with model data for accurate behavior.

### Integration Tests:
- **Controller and Service Interactions**: Testing the interactions between controllers and services for creating, editing, and deleting models.
- **End-to-End Data Flow**: Simulating the flow of data from the user interface through the service layer and persistence.

### End-to-End (E2E) Tests:
- **Application Navigation**: Ensuring smooth navigation across the application, testing critical paths.
- **Model Creation/Editing/Deletion**: Full-cycle tests that simulate user behavior when creating, editing, and deleting models through the UI.

---

## Why Spock & Groovy?

We chose **Spock** and **Groovy** for testing because of their simplicity and seamless integration with Java. The Groovy language's concise syntax allows us to write clear and maintainable tests, while Spock’s behavior-driven approach (`given`, `when`, `then` blocks) helps structure tests in an intuitive way.

In addition to ease of use, **Spock** provides powerful mocking and stubbing capabilities, making it an excellent choice for testing services and databases in isolation.

---

## Project Documentation

For a detailed explanation of our testing approach, methodologies, and the project structure, you can check out the [full project documentation here](https://docs.google.com/document/d/1uKrzy9zqu73CLYedover27uVYHMdrY2Fv1uhILMoraI/edit?usp=sharing).


