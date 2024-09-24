package ownersTemplatesTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print


@SpringBootTest(classes = PetClinicApplication)
@AutoConfigureMockMvc
class FindOwnersSpec extends Specification{

    @Autowired
    private MockMvc mockMvc

    def "GET /owners returns owners search page"() {
        when: "A GET request is made to /owners"
        def result = mockMvc.perform(get("/owners"))

        then: "The view name is 'owners/ownersList' and the status is OK (200)"
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("owners/ownersList")) // Update expected view name
                .andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
                .andDo(print())
    }

    def "GET /owners finds owners"() {
        when: "A GET request is made to /owners/find with valid parameters"
        def result = mockMvc.perform(get("/owners/find")
                .param("lastName", "Smith"))

        then: "The response is a successful rendering of the findOwners page"
        result.andExpect(MockMvcResultMatchers.status().isOk()) // Expecting a successful response
                .andExpect(MockMvcResultMatchers.view().name("owners/findOwners")) // Expect the correct view name
                .andExpect(MockMvcResultMatchers.model().attributeExists("owner")) // Check for the presence of the owner attribute
                .andDo(print())
    }

}
