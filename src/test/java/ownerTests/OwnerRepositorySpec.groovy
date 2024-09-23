package ownerTests

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.samples.petclinic.owner.PetType
import spock.lang.Specification

class OwnerRepositorySpec extends Specification {

    OwnerRepository ownerRepository = Mock(OwnerRepository)

    def "test findById method"() {
        given:
        def owner = new Owner(id: 1, lastName: "Doe")

        ownerRepository.findById(1) >> owner

        when:
        def foundOwner = ownerRepository.findById(1)

        then:
        foundOwner != null
        foundOwner.lastName == "Doe"
    }

    def "test findByLastName method"() {
        given:
        def owner1 = new Owner(id: 1, lastName: "Doe")
        def owner2 = new Owner(id: 2, lastName: "Doe")
        Page<Owner> ownerPage = new PageImpl([owner1, owner2])

        ownerRepository.findByLastName("Doe", Pageable.unpaged()) >> ownerPage

        when:
        Page<Owner> foundOwners = ownerRepository.findByLastName("Doe", Pageable.unpaged())

        then:
        foundOwners != null
        foundOwners.content.size() == 2
    }

    def "test findPetTypes method"() {
        given:
        def petTypes = [new PetType(name: "Dog"), new PetType(name: "Cat")]

        ownerRepository.findPetTypes() >> petTypes

        when:
        def foundPetTypes = ownerRepository.findPetTypes()

        then:
        foundPetTypes.size() == 2
        foundPetTypes*.name == ["Dog", "Cat"]
    }

    def "test save method"() {
        given:
        def owner = new Owner(id: 1, lastName: "Doe")

        when:
        ownerRepository.save(owner)

        then:
        1 * ownerRepository.save(owner)
    }

    def "test findAll method"() {
        given:
        def owner1 = new Owner(id: 1, lastName: "Doe")
        def owner2 = new Owner(id: 2, lastName: "Smith")
        Page<Owner> ownerPage = new PageImpl([owner1, owner2])

        ownerRepository.findAll(Pageable.unpaged()) >> ownerPage

        when:
        Page<Owner> foundOwners = ownerRepository.findAll(Pageable.unpaged())

        then:
        foundOwners != null
        foundOwners.content.size() == 2
    }
}
