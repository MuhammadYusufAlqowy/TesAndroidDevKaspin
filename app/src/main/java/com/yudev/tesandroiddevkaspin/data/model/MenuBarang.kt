package com.yudev.tesandroiddevkaspin.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class MenuBarang(
    @PrimaryKey(autoGenerate = true)
    val id_barang : Int? = null,
    var kode_barang : String,
    var nama_barang : String,
    var stok:Int
):Parcelable
