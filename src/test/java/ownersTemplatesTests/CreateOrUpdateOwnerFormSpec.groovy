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
class CreateOrUpdateOwnerFormSpec extends Specification {

    @Autowired
    MockMvc mockMvc



    def "Owner form should render all required input fields and dynamic button"() {
        when: "The user navigates to the owner form"
        def result = mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andReturn()

        then: "The page contains the correct input fields for Owner"
        def content = result.response.contentAsString

        content.contains("First Name")
        content.contains("Last Name")
        content.contains("Address")
        content.contains("City")
        content.contains("Telephone")

        and: "The page contains a submit button with dynamic text"
        content.contains("Add Owner") || content.contains("Update Owner")
    }
}
