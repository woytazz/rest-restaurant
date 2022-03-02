package pl.pas.rest.controller;

import lombok.SneakyThrows;
import pl.pas.rest.dto.LoginDTO;
import pl.pas.rest.security.JWSGeneratorVerifier;
import pl.pas.rest.security.JWTGeneratorVerifier;
import pl.pas.rest.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/login")
@RequestScoped
public class LoginController {
    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @Inject
    private UserService userService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticate(@Valid LoginDTO loginDTO) {
        Credential credential = new UsernamePasswordCredential(loginDTO.getLogin(), new Password(loginDTO.getPassword()));
        CredentialValidationResult result = identityStoreHandler.validate(credential);

        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return Response.accepted()
                    .entity(JWTGeneratorVerifier.generateJWTString(result))
                    .build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/_self")
    @Produces(MediaType.APPLICATION_JSON)
    @SneakyThrows
    public Response findSelf(@Context SecurityContext securityContext) {
        return Response.ok()
                .entity(userService.findUserByLogin(securityContext.getUserPrincipal().getName()))
                .build();
    }
}
