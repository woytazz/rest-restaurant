package pl.pas.rest.controller;

import pl.pas.rest.dto.TableAllocationPostDTO;
import pl.pas.rest.exceptions.BadEndDateException;
import pl.pas.rest.exceptions.TableAllocationNotFoundException;
import pl.pas.rest.service.TableAllocationService;

import javax.ejb.ObjectNotFoundException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/allocations")
@RequestScoped
public class TableAllocationController {
    @Inject
    private TableAllocationService tableAllocationService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postTableAllocation(@Valid TableAllocationPostDTO tableAllocationPostDTO) {
            try {
                if (tableAllocationService.createTableAllocation(tableAllocationPostDTO)) {
                    return Response.status(201)
                            .build();
                } else {
                    return Response.status(409)
                            .build();
                }
            } catch (ObjectNotFoundException e) {
                return Response.status(404, e.getMessage())
                        .build();
            }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTableAllocation(@PathParam("id") UUID uuid) {
        try {
            return Response.ok().entity(tableAllocationService.readTableAllocation(uuid))
                    .build();
        } catch (TableAllocationNotFoundException e) {
            return Response.status(404, e.getMessage())
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTableAllocations() {
        return Response.ok().entity(tableAllocationService.readAllTableAllocations())
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTableAllocation(@PathParam("id") UUID uuid) {
        try {
            if (tableAllocationService.deleteTableAllocation(uuid)) {
                return Response.ok()
                        .build();
            } else {
                return Response.status(409)
                        .build();
            }
        } catch (TableAllocationNotFoundException e) {
            return Response.status(404, e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQueryTableAllocations(
            @QueryParam("type") String type,
            @QueryParam("time") String time,
            @QueryParam("uuid") UUID uuid) {
        try {
            return Response.ok().entity(tableAllocationService.queryTableAllocations(type, time, uuid))
                    .build();
        } catch (BadRequestException e) {
            return Response.status(400, e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/end/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endTableAllocation(@PathParam("id") UUID uuid) {
        try {
            tableAllocationService.endTableAllocation(uuid);
            return Response.ok()
                    .build();
        } catch (TableAllocationNotFoundException e) {
            return Response.status(404, e.getMessage())
                    .build();
        } catch (BadEndDateException e) {
            return Response.status(409, e.getMessage())
                    .build();
        }
    }
}
