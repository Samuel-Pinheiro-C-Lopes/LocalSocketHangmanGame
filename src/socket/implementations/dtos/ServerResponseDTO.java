package socket.implementations.dtos;

import game.interfaces.Match;
import socket.interfaces.dtos.ServerResponse;

public record ServerResponseDTO(Match match) implements ServerResponse{ }
