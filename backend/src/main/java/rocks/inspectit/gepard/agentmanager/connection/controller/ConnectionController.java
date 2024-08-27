/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionResponse;
import rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService;

/**
 * Controller for handling agent connection requests. Holds the POST endpoint for handling
 * connection requests from agents and the GET endpoints for fetching all or one connection by id.
 */
@RestController
@RequestMapping("/api/v1/connections")
@RequiredArgsConstructor
public class ConnectionController {

  private final ConnectionService connectionService;

  @PostMapping
  public ResponseEntity<Void> connect(@Valid @RequestBody CreateConnectionRequest connectRequest) {
    CreateConnectionResponse connectionDto = connectionService.handleConnectRequest(connectRequest);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(connectionDto.id())
                .toUri())
        .build();
  }

  @GetMapping
  public ResponseEntity<List<CreateConnectionResponse>> getConnections() {
    return ResponseEntity.ok(connectionService.getConnections());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CreateConnectionResponse> getConnection(@PathVariable UUID id) {
    return ResponseEntity.ok(connectionService.getConnection(id));
  }
}
