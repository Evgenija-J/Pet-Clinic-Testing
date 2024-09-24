package ownerTests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.samples.petclinic.PetClinicApplication
import org.springframework.samples.petclinic.owner.Owner
import org.springframework.samples.petclinic.owner.OwnerController
import org.springframework.samples.petclinic.owner.OwnerRepository
import org.springframework.web.servlet.ModelAndView
import spock.lang.Specification
import spock.lang.Unroll
import org.springframework.validation.BindingResult
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@SpringBootTest(classes = PetClinicApplication)
class OwnerControllerSpec extends Specification {

    @Autowired
    OwnerController ownerController

    @Autowired
    OwnerRepository ownerRepository

    Owner owner
    Owner owner2
    Integer ownerId

    def setup() {
        owner = new Owner(firstName: "Test", lastName: "Owner", address: "789 Oak St", city: "Springfield", telephone: "5551234567")
        owner2 = new Owner(firstName: "Test2", lastName: "Owner2", address: "Address", city: "City", telephone: "1234567890")
        ownerRepository.save(owner)
        ownerRepository.save(owner2)
        ownerId = owner.getId()
    }

    def cleanup() {
        ownerRepository.deleteAll()
    }

    @Unroll
    def "should show owner details"() {
        when: "We fetch the owner details"
        ModelAndView mav = ownerController.showOwner(ownerId)

        then: "The model should contain the correct owner"
        Owner expectedOwner = ownerRepository.findById(ownerId).orElse(null) 
        Owner ownerInModel = mav.model.get("owner")

        ownerInModel.equals(expectedOwner)
    }

    @Unroll
    def "should show the form to create a new owner"() {
        when: "We access the new owner creation form"
        String viewName = ownerController.initCreationForm([:])

        then: "The view name should be correct"
        viewName == "owners/createOrUpdateOwnerForm"
    }

    @Unroll
    def "should create a new owner successfully"() {
        given: "A new owner"
        owner

        and: "A binding result without errors"
        BindingResult result = Mock(BindingResult)
        result.hasErrors() >> false

        and: "Redirect attributes"
        RedirectAttributes redirectAttributes = Mock(RedirectAttributes)

        when: "The creation form is processed"
        String viewName = ownerController.processCreationForm(owner, result, redirectAttributes)

        then: "The owner should be saved and redirected to the owner details"
        ownerRepository.findById(owner.getId()) != null
        redirectAttributes.addFlashAttribute("message", "New Owner Created")
        viewName == "redirect:/owners/${owner.getId()}"
    }

    @Unroll
    def "should handle errors during owner creation"() {
        given: "An owner with validation errors"
        owner.setAddress("")
        owner.setCity("")
        owner.setTelephone("") // Trigger validation error

        and: "A binding result with errors"
        BindingResult result = Mock(BindingResult)
        result.hasErrors() >> true

        and: "Redirect attributes"
        RedirectAttributes redirectAttributes = Mock(RedirectAttributes)

        when: "The creation form is processed"
        String viewName = ownerController.processCreationForm(owner, result, redirectAttributes)

        then: "The owner should not be saved and should show the form again"
        ownerRepository.findById(owner.getId()) == null
        redirectAttributes.addFlashAttribute("error", "There was an error in creating the owner.")
        viewName == "owners/createOrUpdateOwnerForm"
    }

    @Unroll
    def "should update an existing owner"() {
        given: "An existing owner"
        owner = ownerRepository.findById(1) // Assuming owner ID 1 exists
        owner.setFirstName("UpdatedName")

        and: "A binding result without errors"
        BindingResult result = Mock(BindingResult)
        result.hasErrors() >> false

        and: "Redirect attributes"
        RedirectAttributes redirectAttributes = Mock(RedirectAttributes)

        when: "The update form is processed"
        String viewName = ownerController.processUpdateOwnerForm(owner, result, 1, redirectAttributes)

        then: "The owner should be updated and redirected to the owner details"
        ownerRepository.findById(1).getFirstName() == "UpdatedName"
        redirectAttributes.addFlashAttribute("message", "Owner Values Updated")
        viewName == "redirect:/owners/1"
    }

    @Unroll
    def "should handle errors during owner update"() {
        given: "An existing owner"
        owner = ownerRepository.findById(1) // Assuming owner ID 1 exists
        owner.setFirstName("") // Trigger validation error

        and: "A binding result with errors"
        BindingResult result = Mock(BindingResult)
        result.hasErrors() >> true

        and: "Redirect attributes"
        RedirectAttributes redirectAttributes = Mock(RedirectAttributes)

        when: "The update form is processed"
        String viewName = ownerController.processUpdateOwnerForm(owner, result, 1, redirectAttributes)

        then: "The owner should not be updated and should show the form again"
        redirectAttributes.addFlashAttribute("error", "There was an error in updating the owner.")
        viewName == "owners/createOrUpdateOwnerForm"
    }

    @Unroll
    def "should find owners by last name"() {
        when: "Finding owners with last name"
        def model = Mock(Model)
        String viewName = ownerController.processFindForm(1, new Owner(lastName: lastName), Mock(BindingResult), model)

        then: "The view name and model attributes should be correct"
        viewName == expectedView
        model.addAttribute("listOwners", _) // Verify owners are added to the model

        where:
        lastName           | expectedView
        "Doe"              | "owners/ownersList"
        "NonExistingLast"  | "owners/findOwners"
    }
}
