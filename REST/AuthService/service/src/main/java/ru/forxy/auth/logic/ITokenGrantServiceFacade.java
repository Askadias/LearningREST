package ru.forxy.auth.logic;

import ru.forxy.auth.pojo.Token;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;

/**
 * Entry point into token service business logic
 */
public interface ITokenGrantServiceFacade {

    Iterable<Token> getAllTokens();

    EntityPage<Token> getTokens(final Integer page, final Integer size, final SortDirection sortDirection,
                                final String sortedBy, final Token filter);

    Token getToken(final String tokenID);

    void updateToken(final Token token);

    void createToken(final Token token);

    void deleteToken(final String tokenID);
}
