package pl.pas.rest.controller;

import pl.pas.rest.dto.PasswordDTO;
import pl.pas.rest.dto.UserPostDTO;
import pl.pas.rest.exceptions.UserLoginTakenException;
import pl.pas.rest.exceptions.UserNotFoundException;
import pl.pas.rest.security.JWSGeneratorVerifier;
import pl.pas.rest.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/users")
@RequestScoped
public class UserController {
    @Inject
    private UserService userService;

    @Inject
    HttpServletRequest httpServletRequest;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postUser(@Valid UserPostDTO userPostDTO) {
        try {
            return Response.status(201).entity(userService.createUser(userPostDTO))
                    .build();
        } catch (UserLoginTakenException | UserNotFoundException e) {
            return Response.status(409, e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") UUID uuid) {
        try {
            return Response.ok()
                    .header("If-Match", JWSGeneratorVerifier.generateJWSString(userService.readUser(uuid)))
                    .entity(userService.readUser(uuid))
                    .build();
        } catch (UserNotFoundException e) {
            return Response.status(404, e.getMessage())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok().entity(userService.readAllUsers())
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putUser(@PathParam("id") UUID uuid, @Valid UserPostDTO userPostDTO) {
        if (JWSGeneratorVerifier.validateJWSSignature(httpServletRequest.getHeader("If-Match"))) {
            if (userService.validateUpdateJSON(httpServletRequest.getHeader("If-Match"), uuid, userPostDTO)) {
                try {
                    return Response.ok().entity(userService.updateUser(uuid, userPostDTO))
                            .build();
                } catch (UserNotFoundException e) {
                    return Response.status(404, e.getMessage())
                            .build();
                }
            } else {
                return Response.status(406, "Unexpected modified JSON")
                        .build();
            }
        } else {
            return Response.status(401, "Bad JWS")
                    .build();
        }
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersByLogin(
            @QueryParam("type") String type,
            @QueryParam("login") String login) {

        if (type.equals("one")) {
            try {
                return Response.ok().entity(userService.findUserByLogin(login))
                        .build();
            } catch (UserNotFoundException e) {
                return Response.status(404, e.getMessage())
                        .build();
            }
        } else if (type.equals("many")) {
            return Response.ok().entity(userService.matchUsersByLogin(login))
                    .build();
        } else {
            return Response.status(400, "Bad type query param")
                    .build();
        }
    }

    @POST
    @Path("/active/{id}")
    public Response changeActive(@PathParam("id") UUID uuid) {
        if (JWSGeneratorVerifier.validateJWSSignature(httpServletRequest.getHeader("If-Match"))) {
            if (userService.validateUUID(httpServletRequest.getHeader("If-Match"), uuid)) {
                try {
                    userService.changeUserActivity(uuid);
                    return Response.ok()
                            .build();
                } catch (UserNotFoundException e) {
                    return Response.status(404, e.getMessage())
                            .build();
                }
            } else {
                return Response.status(406, "Unexpected modified JSON")
                        .build();
            }
        } else {
            return Response.status(401, "Bad JWS")
                    .build();
        }
    }

    @POST
    @Path("/password/{id}")
    public Response changePassword(@PathParam("id") UUID uuid, @Valid PasswordDTO passwordDTO) {
        if (JWSGeneratorVerifier.validateJWSSignature(httpServletRequest.getHeader("If-Match"))) {
            if (userService.validateUUID(httpServletRequest.getHeader("If-Match"), uuid)) {
                try {
                    userService.updatePassword(uuid, passwordDTO);
                    return Response.ok()
                            .build();
                } catch (UserNotFoundException e) {
                    return Response.status(404, e.getMessage())
                            .build();
                } catch (BadRequestException e) {
                    return Response.status(400, e.getMessage())
                            .build();
                }
            } else {
                return Response.status(406, "Unexpected modified JSON")
                        .build();
            }
        } else {
            return Response.status(401, "Bad JWS")
                    .build();
        }
    }
}
