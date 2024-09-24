package visitTests

import org.springframework.samples.petclinic.owner.Visit
import spock.lang.Specification

import java.time.LocalDate

class VisitSpec extends Specification {

    def "creating a Visit instance with current date and empty description"() {
        when: "creating new Visit instance"
        Visit visit = new Visit()

        then: "date is set to current date"
        visit.getDate() == LocalDate.now()

        and: "description is empty"
        visit.getDescription() == null
    }

    def "testing getDate i setDate methods"() {
        given: "new Visit instance"
        Visit visit = new Visit()

        when: "date is set"
        LocalDate newDate = LocalDate.of(2024, 9, 24)
        visit.setDate(newDate)

        then: "date is updated to new date correctly"
        visit.getDate() == newDate
    }

    def "testing setDescription and getDescription methods"() {
        given: "new Visit instance"
        Visit visit = new Visit()

        when: "description is set"
        visit.setDescription("Testing description")

        then: "description is updated correctly"
        visit.getDescription() == "Testing description"
    }

    def "blank description shouldn't be allowed"() {
        given: "new Visit instance"
        Visit visit = new Visit()

        when: "blank description is set"
        visit.setDescription("")

        then: "description should not be blank"
        visit.getDescription() == ""
    }
}
