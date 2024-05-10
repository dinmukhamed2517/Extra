package kz.sdk.extraa.models

data class User(
    var name: String? = null,
    var lastname: String?= null,
    var pictureUrl: String? = null,
    var cart: Map<String, Product> = emptyMap(),
    var isAdmin:Boolean = false,

)
