package alt.nainapps.sharepaste.privatebinKt.model

import kotlinx.serialization.Serializable

@Serializable
data class PrivatebinPasteMetaData(
    val expire: String? = null,
    val created: Long? = null,
    val time_to_live: Int? = null,
    val icon: String? = null
)
