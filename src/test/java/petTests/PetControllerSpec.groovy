package petTests

import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.samples.petclinic.owner.PetController
import org.springframework.samples.petclinic.owner.PetType
import org.springframework.samples.petclinic.owner.PetValidator
import spock.lang.Specification
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.view.InternalResourceViewResolver
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.bind.WebDataBinder
import java.time.LocalDate

class PetControllerSpec extends Specification {

    MockMvc mockMvc
    PetController petController
    OwnerRepository ownerRepository = Mock()

    def setup() {
        petController = new PetController(ownerRepository)

        def viewResolver = new InternalResourceViewResolver()
        viewResolver.setPrefix("/WEB-INF/jsp/")
        viewResolver.setSuffix(".jsp")

        mockMvc = MockMvcBuilders.standaloneSetup(petController)
                .setViewResolvers(viewResolver)
                .build()
    }

    def "Init creation form displays pet creation form"() {
        given: "An owner exists"
        int ownerId = 1
        Owner owner = new Owner()
        owner.setId(ownerId)
        ownerRepository.findById(ownerId) >> owner

        when: "GET request to /owners/{ownerId}/pets/new"
        def response = mockMvc.perform(get("/owners/{ownerId}/pets/new", ownerId))

        then: "Status is OK and view is the pet creation form"
        response.andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"))
                .andExpect(model().attributeExists("pet"))
    }

    def "Process creation form with valid data redirects to owner details"() {
        given: "Valid pet data and owner"
        int ownerId = 1
        Owner owner = new Owner()
        owner.setId(ownerId)
        ownerRepository.findById(ownerId) >> owner
        ownerRepository.findPetTypes() >> [new PetType(id: 1, name: "Dog")]

        when: "POST request to /owners/{ownerId}/pets/new with valid data"
        def response = mockMvc.perform(post("/owners/{ownerId}/pets/new", ownerId)
                .param("name", "Buddy")
                .param("birthDate", "2015-05-20")
                .param("type.id", "1"))

        then: "Redirects to owner details page"
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/owners/1"))
                .andExpect(flash().attribute("message", "New Pet has been Added"))
        1 * ownerRepository.save(_)
    }

    def "Process creation form with future birth date shows error"() {
        given: "An owner"
        int ownerId = 1
        Owner owner = new Owner()
        owner.setId(ownerId)
        ownerRepository.findById(ownerId) >> owner
        ownerRepository.findPetTypes() >> [new PetType(id: 1, name: "Dog")]

        when: "POST request to /owners/{ownerId}/pets/new with future birth date"
        def futureDate = LocalDate.now().plusDays(1).toString()
        def response = mockMvc.perform(post("/owners/{ownerId}/pets/new", ownerId)
                .param("name", "Buddy")
                .param("birthDate", futureDate)
                .param("type.id", "1"))

        then: "Shows validation error for birthDate"
        response.andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"))
                .andExpect(model().attributeHasFieldErrors("pet", "birthDate"))
    }

    def "Find pet returns new Pet when petId is null"() {
        given: "An owner"
        int ownerId = 1
        Owner owner = new Owner()
        owner.setId(ownerId)
        ownerRepository.findById(ownerId) >> owner

        when: "Accessing /owners/{ownerId}/pets/new"
        def pet = petController.findPet(ownerId, null)

        then: "A new Pet instance is returned"
        pet != null
        pet.isNew()
    }

    def "InitBinder for pet sets PetValidator"() {
        given: "A WebDataBinder"
        WebDataBinder binder = new WebDataBinder(null)

        when: "InitBinder is called for pet"
        petController.initPetBinder(binder)

        then: "A PetValidator is set"
        binder.getValidator() instanceof PetValidator
    }

    def "PopulatePetTypes returns list of pet types"() {
        given: "Owner repository returns pet types"
        ownerRepository.findPetTypes() >> [new PetType(id: 1, name: "Dog"), new PetType(id: 2, name: "Cat")]

        when: "populatePetTypes is called"
        def types = petController.populatePetTypes()

        then: "List of pet types is returned"
        types.size() == 2
        types*.name.containsAll(["Dog", "Cat"])
    }
}
