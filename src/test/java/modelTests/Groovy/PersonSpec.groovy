package org.springframework.samples.petclinic.model

import spock.lang.Specification
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory

class PersonSpec extends Specification {

    Validator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator()
    }

    def "Person with valid firstName and lastName passes validation"() {
        given: "A person with valid firstName and lastName"
        Person person = new Person()
        person.setFirstName("John")
        person.setLastName("Doe")

        when: "The person is validated"
        def violations = validator.validate(person)

        then: "No validation errors are found"
        violations.isEmpty()
    }

    def "Person with blank firstName fails validation"() {
        given: "A person with blank firstName"
        Person person = new Person()
        person.setFirstName("")
        person.setLastName("Doe")

        when: "The person is validated"
        def violations = validator.validate(person)

        then: "A validation error is found for firstName"
        violations.size() == 1
        violations.any { it.propertyPath.toString() == "firstName" && it.message.contains("must not be blank") }
    }

    def "Person with blank lastName fails validation"() {
        given: "A person with blank lastName"
        Person person = new Person()
        person.setFirstName("John")
        person.setLastName("")

        when: "The person is validated"
        def violations = validator.validate(person)

        then: "A validation error is found for lastName"
        violations.size() == 1
        violations.any { it.propertyPath.toString() == "lastName" && it.message.contains("must not be blank") }
    }

    def "Person with null firstName fails validation"() {
        given: "A person with null firstName"
        Person person = new Person()
        person.setFirstName(null)
        person.setLastName("Doe")

        when: "The person is validated"
        def violations = validator.validate(person)

        then: "A validation error is found for firstName"
        violations.size() == 1
        violations.any { it.propertyPath.toString() == "firstName" && it.message.contains("must not be blank") }
    }

    def "Person with null lastName fails validation"() {
        given: "A person with null lastName"
        Person person = new Person()
        person.setFirstName("John")
        person.setLastName(null)

        when: "The person is validated"
        def violations = validator.validate(person)

        then: "A validation error is found for lastName"
        violations.size() == 1
        violations.any { it.propertyPath.toString() == "lastName" && it.message.contains("must not be blank") }
    }

    def "Person with blank firstName and lastName fails validation with two errors"() {
        given: "A person with blank firstName and lastName"
        Person person = new Person()
        person.setFirstName("")
        person.setLastName("")

        when: "The person is validated"
        def violations = validator.validate(person)

        then: "Validation errors are found for both firstName and lastName"
        violations.size() == 2
        violations.any { it.propertyPath.toString() == "firstName" }
        violations.any { it.propertyPath.toString() == "lastName" }
    }

    def "Person getters and setters function correctly"() {
        given: "A new person"
        Person person = new Person()

        when: "Setting firstName and lastName"
        person.setFirstName("Jane")
        person.setLastName("Smith")

        then: "Getters return the correct values"
        person.getFirstName() == "Jane"
        person.getLastName() == "Smith"
    }

    def "Person is an instance of BaseEntity"() {
        given: "A new person"
        Person person = new Person()

        expect: "Person is an instance of BaseEntity"
        person instanceof BaseEntity
    }

    def "Person class is annotated with @MappedSuperclass"() {
        when: "Retrieving annotations from Person class"
        def annotations = Person.class.getAnnotations()

        then: "Person class has @MappedSuperclass annotation"
        annotations.any { it.annotationType().simpleName == 'MappedSuperclass' }
    }
}
