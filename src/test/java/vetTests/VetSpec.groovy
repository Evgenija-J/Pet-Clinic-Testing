package vetTests

import org.springframework.samples.petclinic.vet.Specialty
import org.springframework.samples.petclinic.vet.Vet
import spock.lang.Specification

class VetSpec extends Specification {

    def "should add specialty to vet"() {
        given: "vet and specialty"
        Vet vet = new Vet()
        Specialty specialty = new Specialty()
        specialty.setName("Speciality name")

        when: "specialty is added to vet"
        vet.addSpecialty(specialty)

        then: "vet should have one specialty"
        vet.getNrOfSpecialties() == 1
        vet.getSpecialties().contains(specialty)
    }

    def "should return an unmodifiable list of specialties"() {
        given: "vet with specialties"
        Vet vet = new Vet()
        Specialty specialty1 = new Specialty()
        specialty1.setName("Speciality name 1")
        Specialty specialty2 = new Specialty()
        specialty2.setName("Speciality name 2")
        vet.addSpecialty(specialty1)
        vet.addSpecialty(specialty2)

        when: "specialties list is retrieved"
        List<Specialty> specialties = vet.getSpecialties()

        then: "list is unmodifiable and sorted by name"
        specialties.size() == 2
        specialties[0].name == "Speciality name 1"
        specialties[1].name == "Speciality name 2"
        specialties.getClass().name.contains("Unmodifiable")
    }

    def "should return correct number of specialties"() {
        given: "vet with no specialties"
        Vet vet = new Vet()

        when: "number of specialties is checked"
        int numberOfSpecialties = vet.getNrOfSpecialties()

        then: "number of specialties should be 0"
        numberOfSpecialties == 0

        when: "specialty is added"
        Specialty specialty = new Specialty()
        specialty.setName("New speciality")
        vet.addSpecialty(specialty)

        then: "number of specialties should increase"
        vet.getNrOfSpecialties() == 1
    }

}
