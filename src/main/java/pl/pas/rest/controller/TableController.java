package pl.pas.rest.controller;

import pl.pas.rest.dto.TablePostDTO;
import pl.pas.rest.exceptions.TableNotFoundException;
import pl.pas.rest.exceptions.TableNumberTakenException;
import pl.pas.rest.service.TableService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/tables")
@RequestScoped
public class TableController {
    @Inject
    private TableService tableService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTable(@Valid TablePostDTO tablePostDTO) {
            try {
                return Response.status(201).entity(tableService.createTable(tablePostDTO))
                        .build();
            } catch (TableNumberTakenException | TableNotFoundException e) {
                return Response.status(409, e.getMessage())
                        .build();
            }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTable(@PathParam("id") UUID uuid) {
        try {
            return Response.ok().entity(tableService.readTable(uuid))
                    .build();
        } catch (TableNotFoundException e) {
            return Response.status(404, e.getMessage())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTables() {
        return Response.ok().entity(tableService.readAllTables())
                .build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putTable(@PathParam("id") UUID uuid, @Valid TablePostDTO tablePostDTO) {
            try {
                return Response.ok().entity(tableService.updateTable(uuid, tablePostDTO))
                        .build();
            } catch (TableNotFoundException e) {
                return Response.status(404, e.getMessage())
                        .build();
            }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTable(@PathParam("id") UUID uuid) {
        try {
            if (tableService.deleteTable(uuid)) {
                return Response.ok()
                        .build();
            } else {
                return Response.status(409)
                        .build();
            }
        } catch (TableNotFoundException e) {
            return Response.status(404, e.getMessage())
                    .build();
        }
    }
}
