package rocks.gepard.backend

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface AgentRepository: JpaRepository<Agent, Long> {

}
