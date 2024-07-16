package rocks.gepard.backend.application

import org.springframework.stereotype.Service
import rocks.gepard.backend.domain.model.Agent
import rocks.gepard.backend.infrastructure.incoming.model.AgentDto
import rocks.gepard.backend.infrastructure.incoming.model.AgentResponseDto

@Service
class AgentService {

    private val agentCache: AgentCache<String, Agent> = AgentCache()

    fun storeAgent(agent: Agent?) {
        agent?.let {
            agentCache.set(it.name, it)
        }
    }

    fun getAllAgents(): MutableList<AgentResponseDto> {
        val agent1 = AgentResponseDto()
        agent1.name("Huhn")
        agent1.healthState("alive")
        agent1.javaversion("21")
        agent1.otelversion("1.0.1")

        val agent2 = AgentResponseDto()
        agent2.name("Ente")
        agent2.javaversion("8")
        agent2.otelversion("1.0.5")

        val agent3 = AgentResponseDto()
        agent3.name("Henne")
        agent3.healthState("alive")
        agent3.config("config")
        agent3.javaversion("17")
        agent3.otelversion("1.0.4")

        return mutableListOf(agent3, agent2, agent1)

//        return agentCache.getAllAgents()
    }

    fun getAgentByName(agentName: String?): AgentResponseDto? {
        return agentName
            ?.let { agentCache.get(it) }
            ?.let(Agent::toResponseDto)
    }

//    val agentMap = mutableMapOf();
}

fun Agent.toResponseDto(): AgentResponseDto {
    return AgentResponseDto().name(this.name)
        .javaversion(this.getJavaVersion())
        .otelversion(this.getOtelVersion())
        .config(this.configuration.toString())
}
