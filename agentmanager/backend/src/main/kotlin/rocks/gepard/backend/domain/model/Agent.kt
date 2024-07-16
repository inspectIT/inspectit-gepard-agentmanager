package rocks.gepard.backend.domain.model

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import rocks.gepard.backend.domain.valueObjects.AgentMetaData
import rocks.gepard.backend.domain.valueObjects.Configuration
import java.time.LocalDateTime

//@Entity
class Agent(
    var name: String,
    var registrationTime: LocalDateTime = LocalDateTime.now(),
    var agentMetadata: AgentMetaData,
) {

    @Id
    @GeneratedValue
    var id: Long? = null

    val configuration: Configuration
        get() = Configuration("configuration")

    fun getJavaVersion(): String {
        return agentMetadata.javaVersion
    }

    fun getOtelVersion(): String {
        return agentMetadata.otelVersion
    }

    override fun toString(): String {
        return "Name='$name', Registrationtime='$registrationTime', " +
                "agentMetaData='$agentMetadata'"
    }

}