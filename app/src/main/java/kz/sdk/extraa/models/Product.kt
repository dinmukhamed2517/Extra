package kz.sdk.extraa.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var id:String? = null,
    var title:String? = null,
    var img:String? = null,
    var description:String? = null,
    var calories:Int? = null,
): Parcelable