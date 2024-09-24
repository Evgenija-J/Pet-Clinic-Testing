package ownersTemplatesTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@SpringBootTest(classes = PetClinicApplication)
@AutoConfigureMockMvc
class OwnerDetailsSpec extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    OwnerRepository ownerRepository

    def "should display owner information"() {
        given: "An existing owner"
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setTelephone("1234567890");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");

        ownerRepository.save(owner)

        when: "A request is made to view the owner's details"
        def response = mockMvc.perform(get("/owners/${owner.id}"))
                .andExpect(status().isOk())
                .andReturn().response

        then: "The owner's details are displayed"
        response.contentAsString.contains(owner.getFirstName())
    }

    def "should display pets and visits information"() {
        given: "An existing owner with pets and visits"
        def ownerId = 1

        when: "A request is made to view the owner's details"
        def response = mockMvc.perform(get("/owners/${ownerId}"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andReturn().response

        then: "The pets and visits information is displayed correctly"
        response.contentAsString.contains("Pets and Visits")
        response.contentAsString.contains("Visit Date")
        response.contentAsString.contains("Description")
    }
}
