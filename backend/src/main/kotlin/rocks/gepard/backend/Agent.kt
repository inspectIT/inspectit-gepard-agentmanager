package rocks.gepard.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Agent(var name: String) {

    @Id
    @GeneratedValue
    var id: Long? = null

    override fun toString(): String {
        return "Name='$name'"
    }
}