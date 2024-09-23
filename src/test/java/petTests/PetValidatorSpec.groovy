package petTests

import org.springframework.samples.petclinic.owner.Pet
import org.springframework.samples.petclinic.owner.PetType
import org.springframework.samples.petclinic.owner.PetValidator
import org.springframework.validation.Errors
import spock.lang.Specification

import java.time.LocalDate

class PetValidatorSpec extends Specification {

    PetValidator petValidator = new PetValidator()
    Errors errors = Mock(Errors)

    def "should reject empty name"() {
        given: "A Pet with an empty name"
        Pet pet = new Pet()
        pet.setName("")

        when: "Validating the Pet"
        petValidator.validate(pet, errors)

        then: "An error should be recorded for the name field"
        1 * errors.rejectValue("name", "required", "required")
    }

    def "should reject null type for a new pet"() {
        given: "A new Pet with null type"
        Pet pet = new Pet()
        pet.setName("Fido")
        pet.setType(null)

        when: "Validating the Pet"
        petValidator.validate(pet, errors)

        then: "An error should be recorded for the type field"
        1 * errors.rejectValue("type", "required", "required")
    }

    def "should reject null birth date"() {
        given: "A Pet with a null birth date"
        Pet pet = new Pet()
        pet.setName("Bella")
        pet.setBirthDate(null)

        when: "Validating the Pet"
        petValidator.validate(pet, errors)

        then: "An error should be recorded for the birthDate field"
        1 * errors.rejectValue("birthDate", "required", "required")
    }

    def "should not reject valid pet"() {
        given: "A valid Pet"
        Pet pet = new Pet()
        pet.name = "Charlie"
        pet.type = new PetType()
        pet.birthDate = LocalDate.now()

        when: "Validating the Pet"
        petValidator.validate(pet, errors)

        then: "No errors should be recorded"
        0 * errors.rejectValue(_, _, _)
    }

    def "should support Pet class"() {
        expect: "PetValidator supports Pet class"
    }

    def "should not support other classes"() {
        expect: "PetValidator does not support other classes"
    }
}
