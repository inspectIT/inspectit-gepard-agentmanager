package rocks.gepard.backend.infrastructure.incoming

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import rocks.gepard.backend.application.AgentService
import rocks.gepard.backend.domain.model.Agent
import rocks.gepard.backend.domain.valueObjects.AgentMetaData
import rocks.gepard.backend.infrastructure.incoming.api.AgentsApi
import rocks.gepard.backend.infrastructure.incoming.model.AgentDto
import rocks.gepard.backend.infrastructure.incoming.model.AgentResponseDto
import java.net.URI

@RestController
class AgentController : AgentsApi {

    private val agentService = AgentService()

    override fun registerAgent(agentDto: AgentDto?): ResponseEntity<AgentDto> {
        val agent = agentDto?.toDomain()
        agentService.storeAgent(agent)

        return agentDto?.let {
            ResponseEntity.created(URI.create("blubb")).build()
        } ?: ResponseEntity.badRequest().build()
    }

    override fun getAllAgents(limit: Int?): ResponseEntity<MutableList<AgentResponseDto>> {
        val agents = agentService.getAllAgents()
        return ResponseEntity.ok(agents)
    }

    override fun getAgentByName(agentName: String?): ResponseEntity<AgentResponseDto> {
        val foundAgent = agentService.getAgentByName(agentName)
        return foundAgent?.let {
            ResponseEntity.ok(foundAgent)
        } ?: ResponseEntity.notFound().build()
    }
}

fun AgentDto.toDomain(): Agent {
    return Agent(
        name = this.name,
        agentMetadata = AgentMetaData(
            otelVersion = this.otelVersion,
            javaVersion = this.javaversion
        )
    )
}

