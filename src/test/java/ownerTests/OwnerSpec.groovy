package ownerTests

import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.Pet
import org.springframework.samples.petclinic.owner.Visit
import spock.lang.Specification

class OwnerSpec extends Specification {

    def "should add a new pet to the owner"() {
        given: "An owner and a new pet"
        Owner owner = new Owner()
        Pet pet = new Pet(name: "Buddy")

        when: "The pet is added to the owner"
        owner.addPet(pet)

        then: "The pet should be in the owner's pet list"
        owner.getPets().contains(pet)
    }

    def "should add an existing pet to the owner"() {
        given: "An owner with an existing pet"
        Owner owner = new Owner()
        Pet existingPet = new Pet(name: "Buddy")
        owner.addPet(existingPet)

        and: "A duplicate pet instance"
        Pet duplicatePet = new Pet(name: "Buddy")

        when: "The duplicate pet is added"
        owner.addPet(duplicatePet)

        then: "The owner's pet list should contain both pet names. This is not addressed in the code."
        owner.getPets().size() == 2
        owner.getPets().contains(existingPet)
    }

    def "should retrieve a pet by name"() {
        given: "An owner with pets"
        Owner owner = new Owner()
        Pet pet1 = new Pet(name: "Buddy")
        Pet pet2 = new Pet(name: "Max")
        owner.addPet(pet1)
        owner.addPet(pet2)

        when: "A pet is retrieved by name"
        Pet retrievedPet = owner.getPet("Buddy")

        then: "The correct pet should be returned"
        retrievedPet == pet1
    }

    def "should return null for a non-existing pet by name"() {
        given: "An owner with pets"
        Owner owner = new Owner()
        owner.addPet(new Pet(name: "Buddy"))

        when: "A non-existing pet is retrieved by name"
        Pet retrievedPet = owner.getPet("Max")

        then: "Should return null"
        retrievedPet == null
    }

    def "should return null for a non-existing pet by id"() {
        given: "An owner with pets"
        Owner owner = new Owner()
        owner.addPet(new Pet(id: 1, name: "Buddy"))

        when: "A non-existing pet is retrieved by id"
        Pet retrievedPet = owner.getPet(2)

        then: "Should return null"
        retrievedPet == null
    }

    def "should throw an exception for an invalid pet identifier when adding a visit"() {
        given: "An owner with a pet"
        Owner owner = new Owner()
        Pet pet = new Pet(name: "Buddy", id: 1)
        owner.addPet(pet)

        and: "A visit to add"
        Visit visit = new Visit()

        when: "An invalid pet id is used to add a visit"
        owner.addVisit(2, visit)

        then: "An exception should be thrown"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Invalid Pet identifier!"
    }

    def "should throw an exception if pet identifier is null when adding a visit"() {
        given: "An owner with a pet"
        Owner owner = new Owner()
        Pet pet = new Pet(name: "Buddy", id: 1)
        owner.addPet(pet)

        and: "A visit to add"
        Visit visit = new Visit()

        when: "Adding a visit with null pet id"
        owner.addVisit(null, visit)

        then: "An exception should be thrown"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Pet identifier must not be null!"
    }

    def "should throw an exception if visit is null when adding a visit"() {
        given: "An owner with a pet"
        Owner owner = new Owner()
        Pet pet = new Pet(name: "Buddy", id: 1)
        owner.addPet(pet)

        when: "Adding a visit that is null"
        owner.addVisit(1, null)

        then: "An exception should be thrown"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Visit must not be null!"
    }
}
