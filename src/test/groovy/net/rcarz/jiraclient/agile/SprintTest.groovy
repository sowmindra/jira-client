package net.rcarz.jiraclient.agile

import net.rcarz.jiraclient.JiraException
import net.rcarz.jiraclient.RestClient
import net.rcarz.jiraclient.RestException
import net.sf.json.JSONSerializer
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertThat
import static org.mockito.Mockito.when

/**
 * Created by pldupont on 2016-05-19.
 */
class SprintTest extends AbstractResourceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    void "Given a RestClient, when calling getAll(), then receive a list of Sprint."() {
        RestClient mockRestClient = "given a REST Client"()
        when(mockRestClient.get(AgileResource.RESOURCE_URI + "board/" + JSONResources.SPRINT_ORIGIN_BOARD_ID + "/sprint"))
                .thenReturn(JSONSerializer.toJSON(JSONResources.LIST_OF_SPRINTS))

        List<Sprint> sprints = Sprint.getAll(mockRestClient, JSONResources.SPRINT_ORIGIN_BOARD_ID);

        assertThat sprints, new IsNot<>(new IsNull())
        assertThat sprints.size(), new IsEqual<Integer>(2)
        "Assert equals to Sprint 37"(sprints.get(0))
    }

    @Test
    void "Given a RestClient, when calling getAll() and use doesn't have access, then throws an 401 error."() {
        RestException unauthorized = new RestException("Do not have access", 401, "Unauthorized")
        RestClient mockRestClient = "given a REST Client"()
        when(mockRestClient.get(AgileResource.RESOURCE_URI + "board/" + JSONResources.SPRINT_ORIGIN_BOARD_ID + "/sprint"))
                .thenThrow(unauthorized)
        expectedException.expect(JiraException.class);
        expectedException.expectMessage("Failed to retrieve a list of Sprint : /rest/agile/1.0/board/" + JSONResources.SPRINT_ORIGIN_BOARD_ID + "/sprint");

        List<Sprint> sprints = Sprint.getAll(mockRestClient, JSONResources.SPRINT_ORIGIN_BOARD_ID);
    }

    @Test
    void "Given a RestClient, when calling getSprint(84), then receive one Sprint."() {
        RestClient mockRestClient = "given a REST Client"()
        when(mockRestClient.get(AgileResource.RESOURCE_URI + "sprint/37"))
                .thenReturn(JSONSerializer.toJSON(JSONResources.SPRINT))

        Sprint sprint = Sprint.get(mockRestClient, 37);

        "Assert equals to Sprint 37"(sprint)
    }

    @Test
    void "Given a RestClient, when calling getSprint(666), then throws an 404 error."() {
        RestException unauthorized = new RestException("Do not have access", 404, "Unauthorized")
        RestClient mockRestClient = "given a REST Client"()
        when(mockRestClient.get(AgileResource.RESOURCE_URI + "sprint/666"))
                .thenThrow(unauthorized)
        expectedException.expect(JiraException.class);
        expectedException.expectMessage("Failed to retrieve Sprint : /rest/agile/1.0/sprint/666");

        Sprint sprint = Sprint.get(mockRestClient, 666);
    }
}
