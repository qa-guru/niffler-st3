package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.model.auth.AuthUserEntity;
import guru.qa.niffler.db.model.auth.Authority;
import guru.qa.niffler.db.model.auth.AuthorityEntity;
import guru.qa.niffler.db.repository.UserRepository;
import guru.qa.niffler.db.repository.UserRepositorySpringJdbc;
import guru.qa.niffler.jupiter.annotation.GenerateUser;
import guru.qa.niffler.model.UserJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static guru.qa.niffler.util.FakerUtils.generateRandomName;

public class DbCreateUserExtension extends CreateUserExtension {

    private static final String DEFAULT_PASSWORD = "12345";
    private final UserRepository userRepository = new UserRepositorySpringJdbc();

    @Override
    protected UserJson createUserForTest(GenerateUser annotation) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(generateRandomName());
        authUser.setPassword(DEFAULT_PASSWORD);
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(new ArrayList<>(Arrays.stream(Authority.values())
                .map(a -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(a);
                    ae.setUser(authUser);
                    return ae;
                }).toList()));

        userRepository.createUserForTest(authUser);
        UserJson result = UserJson.fromEntity(authUser);
        result.setPassword(DEFAULT_PASSWORD);
        return result;
    }

    @Override
    protected List<UserJson> createFriendsIfPresent(GenerateUser annotation) {
        return Collections.emptyList();
    }

    @Override
    protected List<UserJson> createIncomeInvitationsIfPresent(GenerateUser annotation) {
        return Collections.emptyList();
    }

    @Override
    protected List<UserJson> createOutcomeInvitationsIfPresent(GenerateUser annotation) {
        return Collections.emptyList();
    }
}
