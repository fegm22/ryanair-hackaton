package com.ryanair.flights.interconnections;

import com.ryanair.flights.interconnections.action.InterconnectionController;
import com.ryanair.flights.interconnections.domain.Interconnection;
import com.ryanair.flights.interconnections.domain.Leg;
import com.ryanair.flights.interconnections.domain.Route;
import com.ryanair.flights.interconnections.domain.Schedule;
import com.ryanair.flights.interconnections.service.InterconnectionService;
import com.ryanair.flights.interconnections.service.RouteService;
import com.ryanair.flights.interconnections.service.ScheduleService;
import com.ryanair.flights.interconnections.utils.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(InterconnectionController.class)
@Ignore
public class InterconnectingFlightsApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterconnectionService interconnectionService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void interconnectionShouldReturnALeg() throws Exception {

        Leg leg = new Leg("DUB", "MAD", Utils.covertLocalDateTime("2018-02-12T06:15"), Utils.covertLocalDateTime
                ("2018-02-12T09:50"));

        Interconnection interconnection = new Interconnection(0, Arrays.asList(leg));

        when(interconnectionService.execute("DUB", "MAD", Utils.covertLocalDateTime("2018-02-12T05:00"),
                Utils.covertLocalDateTime("2018-02-12T10:00")))
                .thenReturn(Arrays.asList(interconnection));

        this.mockMvc.perform(get("/interconnections?departure=DUB&arrival=MAD&departureDateTime=2018-02-12T05:00&arrivalDateTime=2018-02-12T10:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].stops", is(0)))
                .andExpect(jsonPath("$[0].legs", hasSize(1)))
                .andExpect(jsonPath("$[0].legs[0].departureAirport", is(leg.getDepartureAirport())))
                .andExpect(jsonPath("$[0].legs[0].arrivalAirport", is(leg.getArrivalAirport())))
                .andExpect(jsonPath("$[0].legs[0].departureDateTime", is(leg.getDepartureDateTime())))
                .andExpect(jsonPath("$[0].legs[0].arrivalDateTime", is(leg.getArrivalDateTime())))
        ;

        verify(interconnectionService, times(1)).execute("DUB", "MAD", Utils.covertLocalDateTime
                ("2018-02-12T05:00"), Utils.covertLocalDateTime("2018-02-12T10:00"));
        verifyNoMoreInteractions(interconnectionService);
    }

    @Ignore
    public void testScheduleService() {

        Schedule schedule = scheduleService.getSchedules("DUB", "MAD", 2017, 9);

        Assert.assertNotNull(schedule);
        Assert.assertEquals(schedule.getMonth(), Integer.valueOf(9));
        Assert.assertFalse(schedule.getDays().isEmpty());

    }

    @Ignore
    public void testRouterService() {

        List<Route> routes = routeService.getRoutes();

        Assert.assertNotNull(routes);
        Assert.assertFalse(routes.isEmpty());

    }

    @Before
    public void contextLoads() {

        assertThat(interconnectionService).isNotNull();
    }

}
