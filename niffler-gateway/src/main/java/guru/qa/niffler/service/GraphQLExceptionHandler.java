package guru.qa.niffler.service;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import guru.qa.niffler.ex.ToManySubQueriesException;
import jakarta.annotation.Nonnull;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Service;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

@Service
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(@Nonnull Throwable ex,
                                                @Nonnull DataFetchingEnvironment env) {
        if (ex instanceof ToManySubQueriesException) {
            return GraphQLError.newError()
                    .errorType(BAD_REQUEST)
                    .path(env.getExecutionStepInfo().getPath())
                    .message(ex.getMessage())
                    .build();
        } else {
            return super.resolveToSingleError(ex, env);
        }
    }
}
