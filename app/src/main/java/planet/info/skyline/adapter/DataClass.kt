package planet.info.skyline.adapter


import com.google.gson.annotations.SerializedName

data class LaborCodes(
    @SerializedName("Labor_name")
    val laborName: String,
    @SerializedName("Lalor_ID_PK")
    val lalorIDPK: String
)data class SWOStatus(
        @SerializedName("txt_status")
        var SWOStatus: String,
        @SerializedName("ID_PK")
        var SWOStatusId: String

)