package com.team18.tourister.models

data class PaymentInfo(
    var card_number: String,
    var expiry: String,
    var cvv: String)
