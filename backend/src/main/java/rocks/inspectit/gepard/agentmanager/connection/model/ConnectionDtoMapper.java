/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.model;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import rocks.inspectit.gepard.agentmanager.agent.model.Agent;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionResponse;

/**
 * This class is responsible for mapping Connection objects to ConnectionResponse and
 * ConnectionRequest objects. It is annotated with @Component to be automatically detected by Spring
 * for dependency injection.
 */
@Component
public class ConnectionDtoMapper {

  /**
   * Maps a Connection object to a ConnectionSuccessfulResponse object.
   *
   * @param connection The Connection object to be mapped.
   * @return A ConnectionSuccessfulResponse object containing the mapped data from the Connection
   *     object.
   */
  public CreateConnectionResponse toCreateConnectionResponse(Connection connection) {
    return new CreateConnectionResponse(
        connection.getId(),
        connection.getAgent().getServiceName(),
        connection.getAgent().getGepardVersion(),
        connection.getAgent().getOtelVersion(),
        connection.getAgent().getPid(),
        connection.getAgent().getStartTime().toEpochMilli(),
        connection.getAgent().getJavaVersion());
  }

  /**
   * Maps a CreateConnectionRequest object to a Connection object.
   *
   * @param request The CreateConnectionRequest object to be mapped.
   * @return A Connection object containing the mapped data from the CreateConnectionRequest object.
   */
  public Connection toConnection(CreateConnectionRequest request) {
    return Connection.builder()
        .id(UUID.randomUUID())
        .agent(
            Agent.builder()
                .serviceName(request.serviceName())
                .gepardVersion(request.gepardVersion())
                .otelVersion(request.otelVersion())
                .pid(request.pid())
                .startTime(Instant.ofEpochMilli(request.startTime()))
                .javaVersion(request.javaVersion())
                .build())
        .build();
  }
}
