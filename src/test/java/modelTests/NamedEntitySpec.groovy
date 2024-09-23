package modelTests

import org.springframework.samples.petclinic.model.NamedEntity
import spock.lang.Specification

class NamedEntitySpec extends Specification {

    def "should set and get name"() {
        given: "A NamedEntity instance"
        NamedEntity namedEntity = new NamedEntity()

        when: "Setting the name"
        namedEntity.setName("Fido")

        then: "The name should be correctly returned"
        namedEntity.getName() == "Fido"
    }

    def "should return the name in toString method"() {
        given: "A NamedEntity instance with a name"
        NamedEntity namedEntity = new NamedEntity()
        namedEntity.setName("Whiskers")

        expect: "toString should return the name"
        namedEntity.toString() == "Whiskers"
    }

    def "should handle null name gracefully"() {
        given: "A NamedEntity instance with no name set"
        NamedEntity namedEntity = new NamedEntity()

        expect: "toString should return null"
        namedEntity.toString() == null
    }

    def "should allow updating the name"() {
        given: "A NamedEntity instance with a name"
        NamedEntity namedEntity = new NamedEntity()
        namedEntity.setName("Buddy")

        when: "Updating the name"
        namedEntity.setName("Max")

        then: "The updated name should be returned"
        namedEntity.getName() == "Max"
    }
}
