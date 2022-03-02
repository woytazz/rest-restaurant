package pl.pas.rest.security;

import pl.pas.rest.model.User;
import pl.pas.rest.repository.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.*;

@ApplicationScoped
public class InMemoryIdentityStore implements IdentityStore {
    @Inject
    private UserRepository userRepository;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return IdentityStore.super.getCallerGroups(validationResult);
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            User user = userRepository.findByLoginPasswordActive(usernamePasswordCredential.getCaller(), usernamePasswordCredential.getPasswordAsString());

            return (null != user ? new CredentialValidationResult(user.getLogin(), new HashSet<>(List.of(user.getRole()))) : CredentialValidationResult.INVALID_RESULT);
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }
}
