/* (C) 2024 */
package rocks.inspectit.gepard.agentmanager.connection.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.inspectit.gepard.agentmanager.connection.model.Connection;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.ConnectionDto;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.CreateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.QueryConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.model.dto.UpdateConnectionRequest;
import rocks.inspectit.gepard.agentmanager.connection.service.ConnectionService;
import rocks.inspectit.gepard.agentmanager.exception.ApiError;

/**
 * Controller for handling agent connection requests. Holds the POST endpoint for handling
 * connection requests from agents and the GET endpoints for fetching all or one connection by id.
 */
@RestController
@RequestMapping("/api/v1/connections")
@RequiredArgsConstructor
public class ConnectionController {

  private final ConnectionService connectionService;

  @PostMapping("/{id}")
  @Operation(summary = "Connect an agent to the agent manager.")
  public ResponseEntity<Void> connect(
      @PathVariable String id, @Valid @RequestBody CreateConnectionRequest connectRequest) {
    Connection connection = connectionService.handleConnectRequest(id, connectRequest);
    return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().build().toUri())
        .build();
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update the agent connection.")
  public ResponseEntity<ConnectionDto> update(
      @PathVariable String id, @Valid @RequestBody UpdateConnectionRequest updateRequest) {
    ConnectionDto connection = connectionService.handleUpdateRequest(id, updateRequest);
    return ResponseEntity.ok(connection);
  }

  @GetMapping
  @Operation(summary = "Get all connections.")
  public ResponseEntity<List<ConnectionDto>> getConnections() {
    return ResponseEntity.ok(connectionService.getConnections());
  }

  @PostMapping("/query")
  @Operation(
      summary = "Query connections with support for exact and regex matching",
      description =
          """
        Query connections using a combination of exact matches and regex patterns.
        For regex matching, prefix the pattern with 'regex:'.
        All fields are optional - omitted fields will not be considered in the query.
        """)
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved matching connections",
            content =
                @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ConnectionDto.class)))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid query parameters or regex pattern",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class)))
      })
  public ResponseEntity<List<ConnectionDto>> queryConnections(
      @Valid @RequestBody QueryConnectionRequest query) {
    return ResponseEntity.ok(connectionService.queryConnections(query));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get a connection by id.")
  public ResponseEntity<ConnectionDto> getConnection(@PathVariable String id) {
    return ResponseEntity.ok(connectionService.getConnection(id));
  }
}
