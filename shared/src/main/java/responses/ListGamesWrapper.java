package responses;

import java.util.Collection;

public record ListGamesWrapper(Collection<ListGamesResponse> games) {
}
