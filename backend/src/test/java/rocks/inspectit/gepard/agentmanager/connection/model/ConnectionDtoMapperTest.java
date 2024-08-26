package rocks.inspectit.gepard.agentmanager.connection.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rocks.inspectit.gepard.agentmanager.agent.model.Agent;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionResponse;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionDtoMapperTest {

    private ConnectionDtoMapper connectionDtoMapper;

    @BeforeEach
    public void setUp() {
        connectionDtoMapper = new ConnectionDtoMapper();
    }

    @Test
    void testToConnection() {

        //
        CreateConnectionRequest createConnectionRequest =
                CreateConnectionRequest.builder()
                        .serviceName("agentName")
                        .gepardVersion("gepardVersion")
                        .otelVersion("otelVersion")
                        .pid(123L)
                        .startTime(1719394483600L)
                        .javaVersion("javaVersion")
                        .build();

        Connection connection = connectionDtoMapper.toConnection(createConnectionRequest);

        assertEquals("agentName", connection.getAgent().getServiceName());
        assertEquals("gepardVersion", connection.getAgent().getGepardVersion());
        assertEquals("otelVersion", connection.getAgent().getOtelVersion());
        assertEquals(123L, connection.getAgent().getPid());
        assertEquals(1719394483600L, connection.getAgent().getStartTime().toEpochMilli());
        assertEquals("javaVersion", connection.getAgent().getJavaVersion());
    }

    @Test
    void testToConnectionSuccessfulResponse() {
        UUID randomUUID = UUID.randomUUID();

        Connection connection =
                Connection.builder()
                        .id(randomUUID)
                        .agent(
                                Agent.builder()
                                        .serviceName("agentName")
                                        .gepardVersion("gepardVersion")
                                        .otelVersion("otelVersion")
                                        .pid(123L)
                                        .startTime(Instant.ofEpochMilli(1719394483600L))
                                        .javaVersion("javaVersion")
                                        .build())
                        .build();

        CreateConnectionResponse createConnectionSuccessfulResponse =
                connectionDtoMapper.toCreateConnectionResponse(connection);

        assertEquals(randomUUID, createConnectionSuccessfulResponse.id());
        assertEquals("agentName", createConnectionSuccessfulResponse.serviceName());
        assertEquals("gepardVersion", createConnectionSuccessfulResponse.gepardVersion());
        assertEquals("otelVersion", createConnectionSuccessfulResponse.otelVersion());
        assertEquals(123L, createConnectionSuccessfulResponse.pid());
        assertEquals(1719394483600L, createConnectionSuccessfulResponse.startTime());
        assertEquals("javaVersion", createConnectionSuccessfulResponse.javaVersion());
    }
}