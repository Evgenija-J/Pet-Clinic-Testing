package petTests

import org.springframework.samples.petclinic.model.NamedEntity
import org.springframework.samples.petclinic.owner.Pet
import org.springframework.samples.petclinic.owner.PetType
import org.springframework.samples.petclinic.owner.Visit
import spock.lang.Specification
import java.time.LocalDate

class PetSpec extends Specification {

    def "Pet getters and setters function correctly"() {
        given: "A new pet"
        Pet pet = new Pet()

        when: "Setting name, birthDate, and type"
        pet.setName("Buddy")
        pet.setBirthDate(LocalDate.of(2015, 5, 20))
        PetType petType = new PetType()
        petType.setName("Dog")
        pet.setType(petType)

        then: "Getters return the correct values"
        pet.getName() == "Buddy"
        pet.getBirthDate() == LocalDate.of(2015, 5, 20)
        pet.getType().getName() == "Dog"
    }

    def "Visits collection is initialized and empty"() {
        given: "A new pet"
        Pet pet = new Pet()

        expect: "Visits collection is not null and empty"
        pet.getVisits() != null
        pet.getVisits().isEmpty()
    }

    def "Adding a visit to pet works correctly"() {
        given: "A pet and a visit"
        Pet pet = new Pet()
        Visit visit = new Visit()
        visit.setDescription("Annual check-up")

        when: "Adding the visit to the pet"
        pet.addVisit(visit)

        then: "Visits collection contains the visit"
        pet.getVisits().size() == 1
        pet.getVisits().contains(visit)
    }

    def "Pet is an instance of NamedEntity"() {
        given: "A new pet"
        Pet pet = new Pet()

        expect: "Pet is an instance of NamedEntity"
        pet instanceof NamedEntity
    }

    def "Pet's type association works correctly"() {
        given: "A pet and a pet type"
        Pet pet = new Pet()
        PetType petType = new PetType()
        petType.setName("Cat")

        when: "Associating the pet type with the pet"
        pet.setType(petType)

        then: "Pet's type is set correctly"
        pet.getType().getName() == "Cat"
    }

    def "Pet's name can be set and retrieved from NamedEntity"() {
        given: "A pet"
        Pet pet = new Pet()

        when: "Setting the pet's name"
        pet.setName("Whiskers")

        then: "Name is stored and retrieved correctly"
        pet.getName() == "Whiskers"
    }

    def "Pet's birthDate handles null values"() {
        given: "A pet with no birthDate set"
        Pet pet = new Pet()

        expect: "BirthDate is null"
        pet.getBirthDate() == null

        when: "Setting birthDate to a specific date"
        pet.setBirthDate(LocalDate.of(2020, 1, 1))

        then: "BirthDate is updated correctly"
        pet.getBirthDate() == LocalDate.of(2020, 1, 1)

        when: "Setting birthDate to null"
        pet.setBirthDate(null)

        then: "BirthDate is null again"
        pet.getBirthDate() == null
    }

    def "Visits collection maintains insertion order when dates are equal"() {
        given: "A pet with visits on the same date"
        Pet pet = new Pet()
        Visit visit1 = new Visit()
        visit1.setDate(LocalDate.of(2021, 6, 15))
        visit1.setDescription("First visit")
        Visit visit2 = new Visit()
        visit2.setDate(LocalDate.of(2021, 6, 15))
        visit2.setDescription("Second visit")

        when: "Adding visits to the pet"
        pet.addVisit(visit1)
        pet.addVisit(visit2)

        then: "Visits are ordered by insertion order when dates are equal"
        def visits = pet.getVisits().toList()
        visits[0].getDescription() == "First visit"
        visits[1].getDescription() == "Second visit"
    }
}
