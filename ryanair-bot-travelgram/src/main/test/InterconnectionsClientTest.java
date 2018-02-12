import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.ryanairbot.client.InterconnectionsClient;
import org.ryanairbot.domain.Route;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =
        SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Ignore
public class InterconnectionsClientTest {

    @MockBean
    private InterconnectionsClient interconnectionsClient;

    private static final String DEPARTURE = "MAD";
    private static final String ARRIVAL = "DUB";

//    TODO : make this test works

    @Test
    public void shouldReturnListOfAirports(){

        List<String> routesBetween = interconnectionsClient.findRoutesBetween(DEPARTURE, ARRIVAL);

        assertThat(routesBetween, notNullValue());
    }

}
