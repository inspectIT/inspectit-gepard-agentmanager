/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionResponse;
import rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService;

/**
 * Controller for handling agent connection requests. Holds the POST endpoint for handling
 * connection requests from agents.
 */
@RestController
@RequestMapping("/api/v1/connections")
@RequiredArgsConstructor
public class ConnectionController {

  private final ConnectionService connectionService;

  @PostMapping
  public ResponseEntity<CreateConnectionResponse> connect(
      @Valid @RequestBody CreateConnectionRequest connectRequest) {
    return ResponseEntity.ok(connectionService.handleConnectRequest(connectRequest));
  }

  @GetMapping
  public ResponseEntity<List<ConnectionDto>> getConnections() {
    return ResponseEntity.ok(connectionService.getConnections());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ConnectionDto> getConnection(@PathVariable UUID id) {
    return ResponseEntity.ok(connectionService.getConnection(id));
  }
}
