package responses;

import java.util.Collection;

public record ListGamesResponse(Collection<ListGameResponse> games) {
}
