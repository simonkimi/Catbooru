package ink.z31.catbooru.util


enum class EventType {
    BOORU_CHANGE
}

data class EventMsg(val type: EventType, val msg: String = "")