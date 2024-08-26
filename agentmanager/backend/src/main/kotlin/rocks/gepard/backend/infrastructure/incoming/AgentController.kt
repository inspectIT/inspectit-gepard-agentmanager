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
    private val agentList: MutableList<AgentResponseDto> = mutableListOf()

    init {
        createTestDate()
    }

    override fun registerAgent(agentDto: AgentDto?): ResponseEntity<AgentResponseDto> {
        val agentResponseDto = agentDto?.toResponse()

        // first without deeper logic
//        val agent = agentDto?.toDomain()
//        agentService.storeAgent(agent)
        agentResponseDto?.let {
            agentList.add(0, agentResponseDto)
//        } else {
//            agentResponseDto?.let {
//                agentList.add(0, agentResponseDto)
//            }
        }

        return agentDto?.let {
            ResponseEntity.created(URI.create("blubb")).build()
        } ?: ResponseEntity.badRequest().build()
    }

    override fun getAllAgents(limit: Int?): ResponseEntity<MutableList<AgentResponseDto>> {
//        val agents = agentService.getAllAgents()
//        return ResponseEntity.ok(agents)
        return ResponseEntity.ok(agentList)
    }

    private fun createTestDate() {
        agentList.clear()
        val agent1 = AgentResponseDto()
        agent1.name("Huhn")
        agent1.healthState("alive")
        agent1.javaversion("21")
        agent1.otelversion("1.0.1")
        agentList.add(agent1)

        val agent2 = AgentResponseDto()
        agent2.name("Ente")
        agent2.javaversion("8")
        agent2.otelversion("1.0.5")
        agentList.add(agent2)

        val agent3 = AgentResponseDto()
        agent3.name("Rind")
        agent3.healthState("alive")
        agent3.config("config")
        agent3.javaversion("17")
        agent3.otelversion("1.0.4")
        agentList.add(agent3)
    }
}

fun AgentDto.toDomain(): Agent {
    return Agent(
        name = this.name,
        agentMetadata = AgentMetaData(
            otelVersion = this.otelVersion,
            javaVersion = this.javaVersion
        )
    )
}

fun AgentDto.toResponse(): AgentResponseDto {
    return AgentResponseDto().apply {
        name = this@toResponse.name
        healthState = "alive"
        javaversion = this@toResponse.javaVersion
        otelversion = this@toResponse.otelVersion
        gepardVersion = this@toResponse.gepardVersion
    }
}

