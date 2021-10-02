package com.yudev.tesandroiddevkaspin.data.model


data class OrderBarang(
    var kode_order:String,
    var nama_order:String,
    var list_order:MutableList<TransaksiBarang>?
)
