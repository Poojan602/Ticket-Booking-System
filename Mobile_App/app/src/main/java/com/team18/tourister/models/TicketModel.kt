package com.team18.tourister.models

data class TicketModel(
    var name: String,
    var email: String,
    var date: String,
    var price: String,
    var from: String,
    var to: String,
    var payment_info: PaymentInfo

)