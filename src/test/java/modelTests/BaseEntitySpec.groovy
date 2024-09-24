package modelTests

import org.springframework.samples.petclinic.model.BaseEntity
import spock.lang.Specification

class BaseEntitySpec extends Specification {

    def "testing getId and setId methods"(){
        given: "BaseEntity instance"
        def baseEntity = new BaseEntity()

        when: "setting the id"
        baseEntity.setId(1)

        then: "get the id and check if correct value is returned"
        baseEntity.getId() == 1
    }

    def "testing isNew method for new entity"(){
        given: "BaseEntity instance with no id"
        def baseEntity = new BaseEntity()

        expect: "isNew should return true for the new entity"
        baseEntity.isNew() == true
    }

    def "testing isNew method for already existing entity"(){
        given: "BaseEntity instance with id"
        def baseEntity = new BaseEntity()
        baseEntity.setId(2)

        expect: "isNew should return false for the already existing entity"
        baseEntity.isNew() == false
    }

    def "testing changing id from integer to null"(){
        given: "BaseEntity instance with set id"
        def baseEntity = new BaseEntity()
        baseEntity.setId(3)

        when: "setting id to null"
        baseEntity.setId(null)

        then: "getId should return null"
        baseEntity.getId() == null

        and: "entity should be considered new"
        baseEntity.isNew() == true
    }
}
