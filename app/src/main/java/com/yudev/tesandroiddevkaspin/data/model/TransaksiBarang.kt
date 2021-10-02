package com.yudev.tesandroiddevkaspin.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class TransaksiBarang(
    @PrimaryKey(autoGenerate = true)
    val id_transaksi : Int?=null,
    var kode_barang : String,
    var nama_barang : String,
    var jumlah:Int
):Parcelable
