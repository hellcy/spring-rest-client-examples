package guru.springframework.springrestclientexamples;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestTemplateExamples {

  public static final String API_ROOT = "https://api.predic8.de:443/shop";

  private RestTemplate restTemplate;

  @Before
  public void setUp() throws Exception {
    restTemplate = new RestTemplate();
  }

  @Test
  public void getCategories() {
    String apiUrl = API_ROOT + "/categories/";

    JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);

    System.out.println("Get Categories Response");
    System.out.println(jsonNode.toString());
  }

  @Test
  public void getCustomers() {
    String apiUrl = API_ROOT + "/customers/";

    JsonNode jsonNode = restTemplate.getForObject(apiUrl, JsonNode.class);

    System.out.println("Get Customers Response");
    System.out.println(jsonNode.toString());

  }

  @Test
  public void createCustomer() {
    String apiUrl = API_ROOT + "/customers/";

    // Java object to parse to JSON
    Map<String, Object> postMap = new HashMap<>();
    postMap.put("firstname", "Yuan");
    postMap.put("lastname", "Cheng");

    JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

    System.out.println("Create Customer Response");
    System.out.println(jsonNode.toString());
  }

  @Test
  public void updateCustomer() {
    // create customer to update
    String apiUrl = API_ROOT + "/customers/";

    // Java object to parse to JSON
    Map<String, Object> postMap = new HashMap<>();
    postMap.put("firstname", "Before");
    postMap.put("lastname", "Update");

    JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

    System.out.println("Create Customer Response");
    System.out.println(jsonNode.toString());

    String customerUrl = jsonNode.get("customer_url").textValue();

    String id = customerUrl.split("/")[3];

    System.out.println("Created Customer Id: " + id);

    postMap.put("firstname", "After");

    restTemplate.put(apiUrl + id, postMap);

    JsonNode updatedNode = restTemplate.getForObject(apiUrl + id, JsonNode.class);

    System.out.println("Updated Customer Response");
    System.out.println(updatedNode.toString());
  }

  @Test(expected = HttpClientErrorException.class)
  public void deleteCustomer() {
    // create customer to update
    String apiUrl = API_ROOT + "/customers/";

    // Java object to parse to JSON
    Map<String, Object> postMap = new HashMap<>();
    postMap.put("firstname", "Before");
    postMap.put("lastname", "Update");

    JsonNode jsonNode = restTemplate.postForObject(apiUrl, postMap, JsonNode.class);

    System.out.println("Create Customer Response");
    System.out.println(jsonNode.toString());

    String customerUrl = jsonNode.get("customer_url").textValue();

    String id = customerUrl.split("/")[3];

    System.out.println("Created Customer Id: " + id);

    restTemplate.delete(apiUrl + id);

    System.out.println("Customer deleted");

    // should go to 404
    restTemplate.getForObject(apiUrl + id, JsonNode.class);
  }
}
