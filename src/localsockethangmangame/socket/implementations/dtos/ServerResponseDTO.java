package localsockethangmangame.socket.implementations.dtos;

import localsockethangmangame.game.interfaces.Match;
import localsockethangmangame.socket.interfaces.dtos.ServerResponse;

public record ServerResponseDTO(Match match) implements ServerResponse { }
