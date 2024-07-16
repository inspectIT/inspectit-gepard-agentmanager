package rocks.gepard.backend.infrastructure.incoming

import com.google.gson.Gson
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import rocks.gepard.backend.infrastructure.incoming.model.AgentDto

@WebMvcTest(AgentController::class)
class AgentControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun registerAgentWillReturns201IfAgentsWasCreated() {

        //GIVEN
        val agentDto = AgentDto()
        agentDto.name = "Hase"
        agentDto.javaversion = "17"
        agentDto.otelVersion = "21"

        var gson = Gson()
        val agentJson = gson.toJson(agentDto)

        //WHEN || THEN
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/registerAgent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(agentJson)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun registerAgentWillReturns400WhenContentIsEmpty() {

        //GIVEN || WHEN || THEN
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/registerAgent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StringUtils.EMPTY)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun registerAgentWillReturns415WhenContentIsXML() {

        //GIVEN
        val agentDto = AgentDto()
        agentDto.name = "Hase"
        agentDto.javaversion = "17"
        agentDto.otelVersion = "21"

        var gson = Gson()
        val agentJson = gson.toJson(agentDto)

        //WHEN || THEN
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/registerAgent")
                .contentType(MediaType.APPLICATION_XML)
                .content(agentJson)
        )
            .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType)
    }

    @Test
    fun registerAgentWillReturns415WhenContentIsNull() {

        //GIVEN
        var gson = Gson()
        val agentJson = gson.toJson(null)

        //WHEN || THEN
        mvc.perform(
            MockMvcRequestBuilders.post("/api/v1/registerAgent")
                .contentType(MediaType.APPLICATION_XML)
                .content(agentJson)
        )
            .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType)
    }
}