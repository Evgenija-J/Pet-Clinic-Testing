package ownerTests

import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerController
import org.springframework.samples.petclinic.owner.OwnerRepository
import spock.lang.Specification
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceViewResolver
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.bind.WebDataBinder

class OwnerControllerSpec extends Specification {

    OwnerRepository ownerRepository

    @Autowired
    MockMvc mockMvc

    OwnerController ownerController

    def owners = Mock(OwnerRepository)

    @Before
    def setup() {
        Owner owner = new Owner()
        owner.setId(1)
        owner.setFirstName("John")
        owner.setLastName("Doe")
        ownerRepository.
        ownerController = new OwnerController(ownerRepository)


        def viewResolver = new InternalResourceViewResolver()
        viewResolver.setPrefix("/WEB-INF/jsp/")
        viewResolver.setSuffix(".jsp")

        mockMvc = MockMvcBuilders.standaloneSetup(new OwnerController(ownerRepository)).build()
    }

    def "Init creation form displays owner creation form"() {
        when: "GET request to /owners/new"
        def response = mockMvc.perform(get("/owners/new"))

        then: "Status is OK and view is the owner creation form"
        response.andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attributeExists("owner"))
    }

    def "Process creation form with errors shows error message"() {
        given: "Invalid owner data"
        Owner owner = new Owner()
        owner.setFirstName("")
        owner.setLastName("Doe")

        when: "POST request to /owners/new with invalid data"
        def response = mockMvc.perform(post("/owners/new")
                .param("firstName", "")
                .param("lastName", "Doe"))

        then: "Shows validation error for firstName"
        response.andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"))
                .andExpect(model().attributeHasFieldErrors("owner", "firstName"))
    }

//    def "Find owner returns a list of owners"() {
//        given: "Owner repository returns a list of owners"
//        Owner owner1 = new Owner(firstName: "John", lastName: "Doe")
//        Owner owner2 = new Owner(firstName: "Jane", lastName: "Smith")
//        ownerRepository.findByLastName("Doe", _) >> new PageImpl<>([owner1] as List<Owner>)
//        ownerRepository.findByLastName("Smith", _) >> new PageImpl<>([owner2] as List<Owner>)
//
//        when: "GET request to /owners with last name search"
//        def response = mockMvc.perform(get("/owners")
//                .param("lastName", "Doe"))
//
//        then: "List of owners is returned"
//        response.andExpect(status().isOk())
//                .andExpect(view().name("owners/ownersList"))
//                .andExpect(model().attributeExists("listOwners"))
//                .andExpect(model().attribute("listOwners", contains(owner1))) // Check for the expected owner
//    }

//    def "Show owner returns owner details"() {
//        given: "An existing owner"
//        int ownerId = 1
//        Owner owner = new Owner()
//        owner.setId(ownerId)
//        owner.setFirstName("John")
//        owner.setLastName("Doe")
//        ownerController.findOwner(ownerId) >> Optional.of(owner)
//
//        when: "GET request to /owners/{ownerId}"
//        def response = mockMvc.perform(get("/owners/${ownerId}"))
//
//        then: "Status is OK and view is the owner details"
//        response.andExpect(status().isOk())
//                .andExpect(view().name("owners/ownerDetails"))
//                .andExpect(model().attributeExists("owner"))
//    }

//    def "findOwner should return the Owner when a valid ownerId is provided"() {
//        given:
//        Integer ownerId = 1
//        Owner expectedOwner = new Owner()
//        expectedOwner.setId(ownerId)
//        expectedOwner.setFirstName("John")
//        expectedOwner.setLastName("Doe")
//
//        owners.findById(ownerId) >> expectedOwner
//
//        when:
//        Owner result = ownerController.findOwner(ownerId)
//
//        then:
//        result == expectedOwner
//    }

    def "InitBinder disallows 'id' field"() {
        given: "A WebDataBinder"
        WebDataBinder binder = new WebDataBinder(null)

        when: "InitBinder is called"
        ownerController.setAllowedFields(binder)

        then: "Field 'id' is disallowed"
        binder.getDisallowedFields().contains("id")
    }
}
