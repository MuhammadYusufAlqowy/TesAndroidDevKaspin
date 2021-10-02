package com.yudev.tesandroiddevkaspin.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yudev.tesandroiddevkaspin.data.model.MenuBarang
import com.yudev.tesandroiddevkaspin.data.model.TransaksiBarang

@Dao
interface MenuBarangDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: MenuBarang):Long

    @Delete
    suspend fun delete(item: MenuBarang)

    @Query("DELETE FROM MenuBarang WHERE kode_barang = :kode_barang")
    suspend fun deleteByKodeBarang(kode_barang:String)

    @Update
    suspend fun update(item: MenuBarang)

    @Query("SELECT * FROM MenuBarang")
    fun select() : LiveData<List<MenuBarang>>

    @Query("UPDATE MenuBarang SET stok = :stok WHERE kode_barang = :kode_barang")
    suspend fun updateStokByKodeBarang(kode_barang:String, stok:Int)
}

@Dao
interface TransaksiDao{
    @Query("SELECT * FROM TransaksiBarang")
    fun select() : LiveData<List<TransaksiBarang>>

    @Query("SELECT * FROM TransaksiBarang WHERE kode_barang = :kode_barang LIMIT 1")
    suspend fun selectByKodeBarang(kode_barang:String) : TransaksiBarang?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TransaksiBarang)

    @Delete
    suspend fun delete(item: TransaksiBarang)


    @Query("delete from TransaksiBarang")
    suspend fun deleteAll()
}