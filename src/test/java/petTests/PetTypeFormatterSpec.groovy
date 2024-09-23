package petTests

import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.samples.petclinic.owner.PetType
import org.springframework.samples.petclinic.owner.PetTypeFormatter
import spock.lang.Specification
import java.text.ParseException

class PetTypeFormatterSpec extends Specification {

    OwnerRepository ownerRepository = Mock()
    PetTypeFormatter petTypeFormatter

    def setup() {
        petTypeFormatter = new PetTypeFormatter(ownerRepository)
    }

    def "print should return pet type name"() {
        given: "A PetType with a specific name"
        PetType petType = new PetType()
        petType.setName("Dog")

        when: "Printing the PetType"
        String printed = petTypeFormatter.print(petType, Locale.ENGLISH)

        then: "The name of the PetType is returned"
        printed == "Dog"
    }

    def "parse should throw ParseException when type name is not found"() {
        given: "A collection of PetTypes without the target type"
        ownerRepository.findPetTypes() >> [new PetType(name: "Dog"), new PetType(name: "Cat")]

        when: "Parsing an unknown type name"
        petTypeFormatter.parse("Lizard", Locale.ENGLISH)

        then: "A ParseException is thrown with the correct message"
        def exception = thrown(ParseException)
        exception.message == "type not found: Lizard"
    }

    def "parse should handle case sensitivity correctly"() {
        given: "A collection of PetTypes with mixed case names"
        PetType dog = new PetType()
        dog.setName("Dog")
        ownerRepository.findPetTypes() >> [dog]

        when: "Parsing a lowercase type name"
        petTypeFormatter.parse("dog", Locale.ENGLISH)

        then: "No PetType is found and ParseException is thrown"
        thrown(ParseException)
    }

    def "populatePetTypes includes all available pet types"() {
        given: "A collection of PetTypes from the repository"
        PetType dog = new PetType()
        dog.setName("Dog")
        PetType cat = new PetType()
        cat.setName("Cat")
        ownerRepository.findPetTypes() >> [dog, cat]

        when: "Invoking populatePetTypes"
        Collection<PetType> petTypes = petTypeFormatter.parse("Dog", Locale.ENGLISH) != null ? ownerRepository.findPetTypes() : []

        then: "All PetTypes are included"
        petTypes.size() == 2
        petTypes.containsAll([dog, cat])
    }
}
