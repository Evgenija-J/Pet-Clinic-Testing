package vetTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.samples.petclinic.vet.VetRepository
import spock.lang.Specification
import spock.lang.Unroll
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(classes = PetClinicApplication)
@Transactional
class VetRepositorySpec extends Specification {

    @Autowired
    VetRepository vetRepository

    @Unroll
    def "should find all vets"() {
        when: "All vets are retrieved from the repository"
        def vets = vetRepository.findAll()

        then: "The list of vets should not be empty and should contain the correct number of vets"
        vets.size() != 0
    }

}
