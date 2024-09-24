package visitTemplatesTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.samples.petclinic.owner.Pet
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = PetClinicApplication)
@AutoConfigureMockMvc
class CreateOrUpdateVisitSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    OwnerRepository owners

    def "Pet visit form should render all required input fields and dynamic button"() {
        given: "An owner ID and pet ID"
        int ownerId = 1
        int petId = 1

        when: "The user navigates to the pet visit form"
        def result = mockMvc.perform(get("/owners/${ownerId}/pets/${petId}/visits/new"))
                .andExpect(status().isOk())
                .andReturn()

        then: "The page contains the correct pet details"
        def content = result.response.contentAsString

        content.contains("Pet")
        content.contains("Name")
        content.contains("Birth Date")
        content.contains("Type")
        content.contains("Owner")

        and: "The page contains the input fields for the visit"
        content.contains("Date")
        content.contains("Description")

        and: "The page contains a submit button with the text 'Add Visit'"
        content.contains("Add Visit")
    }

    def "Previous visits should be displayed if they exist"() {
        given: "An owner ID and pet ID with existing visits"
        int ownerId = 1
        int petId = 1

        owners.findById(ownerId) >> {
            def owner = new Owner()
            owner.setId(ownerId)

            Pet pet = new Pet()
            pet.setId(petId)
            owner.getPets().add(pet)
            return owner
        }

        when: "The user navigates to the pet visit form"
        def result = mockMvc.perform(get("/owners/${ownerId}/pets/${petId}/visits/new"))
                .andExpect(status().isOk())
                .andReturn()

        then: "The page contains the correct pet details and previous visits"
        def content = result.response.contentAsString

        content.contains("Pet")
        content.contains("Name")
        content.contains("Birth Date")
        content.contains("Type")
        content.contains("Owner")

        and: "The page contains previous visit entries"
        content.contains("Previous Visits")
        content.contains("Date")
        content.contains("Description")
    }

}
