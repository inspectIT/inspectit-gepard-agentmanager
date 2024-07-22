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

        return agentCache.getAllAgents()
    }

    fun getAgentByName(agentName: String?): AgentResponseDto? {
        return agentName
            ?.let { agentCache.get(it) }
            ?.let(Agent::toResponseDto)
    }
}

fun Agent.toResponseDto(): AgentResponseDto {
    return AgentResponseDto().name(this.name)
        .javaversion(this.getJavaVersion())
        .otelversion(this.getOtelVersion())
        .config(this.configuration.toString())
}
