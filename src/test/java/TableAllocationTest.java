import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import pl.pas.rest.dto.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TableAllocationTest {
    @Test
    public void createTableAllocationTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setIsActive(true);
        userPostDTO.setLogin("tableAllocationTest");
        userPostDTO.setRole("CLIENT");
        userPostDTO.setName("John");
        userPostDTO.setSurname("Smith");
        webTarget.path("/users").request().post(Entity.json(userPostDTO));
        UserGetDTO userGetDTO = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "tableAllocationTest")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);
        UUID userUuid = userGetDTO.getUuid();

        TablePostDTO tablePostDTO = new TablePostDTO();
        tablePostDTO.setTableNumber(Integer.MAX_VALUE - 1);
        tablePostDTO.setDescription("Stolik dla Python Developerów.");
        webTarget.path("/tables").request().post(Entity.json(tablePostDTO));
        Response responseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> tableGetDTOList = responseList.readEntity(new GenericType<>() {
        });
        UUID tableUuid = tableGetDTOList.get(tableGetDTOList.size() - 1).getUuid();

        String jsonString = new JSONObject()
                .put("userUuid", String.valueOf(userUuid))
                .put("tableUuid", String.valueOf(tableUuid))
                .put("startDate", "2030-01-01T12:00")
                .toString();

        webTarget.path("/allocations").request().post(Entity.json(jsonString));

        Response responseAllocationsList = webTarget.path("/allocations").request(MediaType.APPLICATION_JSON).get();
        List<TableAllocationGetDTO> tableAllocationGetDTOList = responseAllocationsList.readEntity(new GenericType<>() {
        });

        LocalDateTime testLocalDateTime = LocalDateTime.parse("2030-01-01T12:00");
        assertEquals(userUuid, tableAllocationGetDTOList.get(tableAllocationGetDTOList.size() - 1).getUserUuid());
        assertEquals(tableUuid, tableAllocationGetDTOList.get(tableAllocationGetDTOList.size() - 1).getTableUuid());
        assertEquals(testLocalDateTime, tableAllocationGetDTOList.get(tableAllocationGetDTOList.size() - 1).getStartDate());
    }
}
