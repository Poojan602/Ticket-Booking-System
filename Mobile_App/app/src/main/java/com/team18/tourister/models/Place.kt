package com.team18.tourister.models

data class Place(
    var Place_name: String,
    var Place_description: String,
//    var type: String,
    var Place_Image: String,
    var Spots: List<SpotPlace>
)