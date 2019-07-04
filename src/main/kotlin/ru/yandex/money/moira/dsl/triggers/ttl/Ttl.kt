package ru.yandex.money.moira.dsl.triggers.ttl

import org.json.JSONObject
import ru.yandex.money.moira.dsl.triggers.MoiraDsl
import ru.yandex.money.moira.dsl.triggers.TriggerState
import ru.yandex.money.moira.dsl.triggers.json.Json
import ru.yandex.money.moira.dsl.triggers.json.json
import java.time.Duration

/**
 * A condition that fires up trigger when no data available for given trigger.
 */
class Ttl private constructor(
    /**
     * A period when no data available.
     */
    private val ttl: Duration,
    /**
     * A state of trigger that will be set when trigger is fired up.
     */
    private val state: TriggerState,
    /**
     * If `false` no notification will be set when trigger state changes from `NO_DATA` to `OK`.
     */
    private val muteNewMetrics: Boolean
) : Json {

    override fun toJson(): JSONObject = json {
        "ttl" to ttl.seconds
        "ttl_state" to state.state
        "mute_new_metrics" to muteNewMetrics
    }

    /**
     * Builder for [Ttl].
     */
    @MoiraDsl
    class Builder {

        /**
         * See [Ttl.ttl]
         */
        var ttl: Duration = Duration.ofMinutes(10)

        /**
         * See [Ttl.state]
         */
        var state: TriggerState =
            TriggerState.NO_DATA

        /**
         * See [Ttl.muteNewMetrics]
         */
        var muteNewMetrics: Boolean = false

        /**
         * Creates time-to-live parameters.
         */
        fun asTtl() = Ttl(ttl = ttl, state = state, muteNewMetrics = muteNewMetrics)
    }
}
