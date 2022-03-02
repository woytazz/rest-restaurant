import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import pl.pas.rest.dto.TableGetDTO;
import pl.pas.rest.dto.TablePostDTO;
import pl.pas.rest.dto.UserGetDTO;
import pl.pas.rest.dto.UserPostDTO;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NegativeTest {
    @Test
    public void negativeCreateUserTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setIsActive(true);
        userPostDTO.setLogin("");
        userPostDTO.setRole("CLIENT");
        userPostDTO.setName("Jan");
        userPostDTO.setSurname("Kowalski");

        Response response = webTarget.path("/users").request().post(Entity.json(userPostDTO));

        assertEquals(400, response.getStatus());
    }

    @Test
    public void negativeCreateTableTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        TablePostDTO tablePostDTO = new TablePostDTO();
        tablePostDTO.setTableNumber(0);
        tablePostDTO.setDescription("Stolik dla testerów.");


        Response response = webTarget.path("/tables").request().post(Entity.json(tablePostDTO));
        assertEquals(400, response.getStatus());
    }

    @Test
    public void uniqueLoginTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setIsActive(true);
        userPostDTO.setLogin("mylogin1234");
        userPostDTO.setRole("CLIENT");
        userPostDTO.setName("Jakub");
        userPostDTO.setSurname("Kowalczyk");

        Response firstPostResponse = webTarget.path("/users").request().post(Entity.json(userPostDTO));

        Response secondPostResponse = webTarget.path("/users").request().post(Entity.json(userPostDTO));

        assertEquals(201, firstPostResponse.getStatus());
        assertEquals(409, secondPostResponse.getStatus());
    }

    @Test
    public void resourceNegativeAllocationTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        UserPostDTO postHappyUser = new UserPostDTO();
        postHappyUser.setIsActive(true);
        postHappyUser.setLogin("positiveAllocationTest");
        postHappyUser.setRole("CLIENT");
        postHappyUser.setName("Wojtek");
        postHappyUser.setSurname("Wesołowski");
        webTarget.path("/users").request().post(Entity.json(postHappyUser));
        UserGetDTO getHappyUser = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "positiveAllocationTest")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);
        UUID happyUserUuid = getHappyUser.getUuid();

        UserPostDTO postSadUser = new UserPostDTO();
        postSadUser.setIsActive(true);
        postSadUser.setLogin("sadAllocationTest");
        postSadUser.setRole("CLIENT");
        postSadUser.setName("Kacper");
        postSadUser.setSurname("Smutalski");
        webTarget.path("/users").request().post(Entity.json(postSadUser));
        UserGetDTO getSadUser = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "sadAllocationTest")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);
        UUID sadUserUuid = getSadUser.getUuid();

        TablePostDTO tablePostDTO = new TablePostDTO();
        tablePostDTO.setTableNumber(Integer.MAX_VALUE - 2);
        tablePostDTO.setDescription("Stolik dla studentów.");
        webTarget.path("/tables").request().post(Entity.json(tablePostDTO));
        Response responseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> tableGetDTOList = responseList.readEntity(new GenericType<>() {
        });
        UUID tableUuid = tableGetDTOList.get(tableGetDTOList.size() - 1).getUuid();

        String happyJsonString = new JSONObject()
                .put("userUuid", String.valueOf(happyUserUuid))
                .put("tableUuid", String.valueOf(tableUuid))
                .put("startDate", "2030-01-01T12:00")
                .toString();

        String sadJsonString = new JSONObject()
                .put("userUuid", String.valueOf(sadUserUuid))
                .put("tableUuid", String.valueOf(tableUuid))
                .put("startDate", "2030-01-01T12:00")
                .toString();

        Response happyResponse = webTarget.path("/allocations").request().post(Entity.json(happyJsonString));
        Response sadResponse = webTarget.path("/allocations").request().post(Entity.json(sadJsonString));

        assertEquals(201, happyResponse.getStatus());
        assertEquals(409, sadResponse.getStatus());
    }
}
