package rocks.gepard.backend.domain.valueObjects

class AgentMetaData(var otelVersion : String, var javaVersion : String) {
    override fun toString(): String {
        return "otelVersion='$otelVersion', " +
                "javaVersion='$javaVersion'"
    }
}