package rocks.gepard.backend.domain.model

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import rocks.gepard.backend.domain.valueObjects.AgentMetaData
import rocks.gepard.backend.domain.valueObjects.RegistrationTime
import java.time.LocalDateTime

//@Entity
class Agent(
    var name: String,
    var registrationTime: LocalDateTime = LocalDateTime.now(),
    var agentMetadata: AgentMetaData
) {

    @Id
    @GeneratedValue
    var id: Long? = null

    override fun toString(): String {
        return "Name='$name', Registrationtime='$registrationTime', " +
                "agentMetaData='$agentMetadata'"
    }

}