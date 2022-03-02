import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void createUserTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setIsActive(true);
        userPostDTO.setLogin("test");
        userPostDTO.setRole("CLIENT");
        userPostDTO.setName("Jan");
        userPostDTO.setSurname("Kowalski");

        webTarget.path("/users").request().post(Entity.json(userPostDTO));

        UserGetDTO userGetDTO = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "test")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);

        assertNotNull(userGetDTO);
        assertEquals("test", userGetDTO.getLogin());
    }

    @Test
    public void readUserTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        UserGetDTO userGetDTO = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "admin1")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);

        assertNotNull(userGetDTO);
        assertEquals("admin1", userGetDTO.getLogin());
        assertFalse(userGetDTO.getIsActive());
        assertEquals("ADMIN", userGetDTO.getRole());
        assertEquals("Adrian", userGetDTO.getName());
        assertEquals("Wojtasik", userGetDTO.getSurname());
    }

    @Test
    public void readAllUsersTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        Response responseList = webTarget.path("/users").request(MediaType.APPLICATION_JSON).get();
        List<UserGetDTO> userGetDTOList = responseList.readEntity(new GenericType<>() {
        });

        UserGetDTO userGetDTO1 = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "admin1")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);
        UserGetDTO userGetDTO2 = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "manager1")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);
        UserGetDTO userGetDTO3 = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "client1")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);

        assertNotNull(userGetDTOList);
        assertTrue(userGetDTOList.contains(userGetDTO1));
        assertTrue(userGetDTOList.contains(userGetDTO2));
        assertTrue(userGetDTOList.contains(userGetDTO3));
    }

    @Test
    public void updateUserTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setIsActive(true);
        userPostDTO.setLogin("updateClient");
        userPostDTO.setRole("CLIENT");
        userPostDTO.setName("Ryszard");
        userPostDTO.setSurname("Nowak");

        webTarget.path("/users").request().post(Entity.json(userPostDTO));

        UserGetDTO userGetDTO = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "updateClient")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);
        String uuid = String.valueOf(userGetDTO.getUuid());

        UserPostDTO updateUserPostDTO = new UserPostDTO();
        updateUserPostDTO.setIsActive(true);
        updateUserPostDTO.setLogin("updateClient");
        updateUserPostDTO.setRole("CLIENT");
        updateUserPostDTO.setName("Pan Ryszard");
        updateUserPostDTO.setSurname("Zaktualizowany");

        webTarget.path("/users/" + uuid).request().put(Entity.json(updateUserPostDTO));

        UserGetDTO updatedUserGetDTO = webTarget.path("/users").path("/login").queryParam("type", "one").queryParam("login", "updateClient")
                .request(MediaType.APPLICATION_JSON).get(UserGetDTO.class);

        assertNotNull(updatedUserGetDTO);
        assertEquals("Pan Ryszard", updatedUserGetDTO.getName());
        assertEquals("Zaktualizowany", updatedUserGetDTO.getSurname());
    }
}
