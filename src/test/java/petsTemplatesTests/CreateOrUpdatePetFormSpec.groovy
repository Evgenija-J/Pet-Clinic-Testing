package ownersTemplatesTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = PetClinicApplication)
@AutoConfigureMockMvc
class CreateOrUpdatePetFormSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    def "Pet form should render all required input fields and dynamic button"() {
        given: "An owner ID"
        int ownerId = 1

        when: "The user navigates to the pet form"
        def result = mockMvc.perform(get("/owners/${ownerId}/pets/new"))
                .andExpect(status().isOk())
                .andReturn()

        then: "The page contains the correct input fields for Pet"
        def content = result.response.contentAsString

        content.contains("Owner")
        content.contains("Name")
        content.contains("Birth Date")
        content.contains("Type")

        and: "The page contains a submit button with dynamic text"
        content.contains("Add Pet") || content.contains("Update Pet")
    }
}
