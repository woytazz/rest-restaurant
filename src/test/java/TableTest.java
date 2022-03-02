import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.pas.rest.dto.TableGetDTO;
import pl.pas.rest.dto.TablePostDTO;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TableTest {
    @Test
    @Order(1)
    public void createTableTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        TablePostDTO tablePostDTO = new TablePostDTO();
        tablePostDTO.setTableNumber(Integer.MAX_VALUE);
        tablePostDTO.setDescription("Stolik dla testerów.");


        webTarget.path("/tables").request().post(Entity.json(tablePostDTO));

        Response responseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> tableGetDTOList = responseList.readEntity(new GenericType<>() {
        });

        assertEquals(Integer.MAX_VALUE, tableGetDTOList.get(tableGetDTOList.size() - 1).getTableNumber());
        assertEquals("Stolik dla testerów.", tableGetDTOList.get(tableGetDTOList.size() - 1).getDescription());
    }

    @Test
    @Order(2)
    public void readTableTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        Response responseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> tableGetDTOList = responseList.readEntity(new GenericType<>() {
        });

        String uuid = String.valueOf(tableGetDTOList.get(0).getUuid());

        TableGetDTO tableGetDTO = webTarget.path("/tables/" + uuid).request(MediaType.APPLICATION_JSON).get(TableGetDTO.class);

        assertEquals(1, tableGetDTO.getTableNumber());
        assertEquals("Stolik dla Java Developerów.", tableGetDTO.getDescription());
    }

    @Test
    @Order(3)
    public void readAllTablesTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        Response responseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> tableGetDTOList = responseList.readEntity(new GenericType<>() {
        });


        String uuid1 = String.valueOf(tableGetDTOList.get(0).getUuid());
        String uuid2 = String.valueOf(tableGetDTOList.get(1).getUuid());
        String uuid3 = String.valueOf(tableGetDTOList.get(2).getUuid());

        TableGetDTO tableGetDTO1 = webTarget.path("/tables/" + uuid1).request(MediaType.APPLICATION_JSON).get(TableGetDTO.class);
        TableGetDTO tableGetDTO2 = webTarget.path("/tables/" + uuid2).request(MediaType.APPLICATION_JSON).get(TableGetDTO.class);
        TableGetDTO tableGetDTO3 = webTarget.path("/tables/" + uuid3).request(MediaType.APPLICATION_JSON).get(TableGetDTO.class);

        assertEquals(1, tableGetDTO1.getTableNumber());
        assertEquals("Stolik dla Java Developerów.", tableGetDTO1.getDescription());
        assertEquals(2, tableGetDTO2.getTableNumber());
        assertEquals("Stolik dla zakochanych.", tableGetDTO2.getDescription());
        assertEquals(3, tableGetDTO3.getTableNumber());
        assertEquals("Stolik dla rodziny.", tableGetDTO3.getDescription());
    }

    @Test
    @Order(4)
    public void updateTableTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        Response responseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> tableGetDTOList = responseList.readEntity(new GenericType<>() {
        });
        String uuid = String.valueOf(tableGetDTOList.get(0).getUuid());

        TablePostDTO tablePostDTO = new TablePostDTO();
        tablePostDTO.setTableNumber(Integer.MAX_VALUE);
        tablePostDTO.setDescription("Zaktualizowany stolik dla Java Developerów.");

        webTarget.path("/tables/" + uuid).request().put(Entity.json(tablePostDTO));

        Response updateResponseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> updateTableGetDTOList = updateResponseList.readEntity(new GenericType<>() {
        });

        assertEquals(1, updateTableGetDTOList.get(0).getTableNumber());
        assertEquals("Zaktualizowany stolik dla Java Developerów.", updateTableGetDTOList.get(0).getDescription());
    }

    @Test
    @Order(5)
    public void deleteTableTest() {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.build();
        WebTarget webTarget = client.target("http://localhost:8080/rest-1.0-SNAPSHOT/api");

        Response responseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> tableGetDTOList = responseList.readEntity(new GenericType<>() {
        });
        String uuid = String.valueOf(tableGetDTOList.get(tableGetDTOList.size() - 1).getUuid());

        webTarget.path("/tables/" + uuid).request().delete();

        Response deleteResponseList = webTarget.path("/tables").request(MediaType.APPLICATION_JSON).get();
        List<TableGetDTO> deleteTableGetDTOList = deleteResponseList.readEntity(new GenericType<>() {
        });

        assertNotEquals(tableGetDTOList, deleteTableGetDTOList);
    }
}
