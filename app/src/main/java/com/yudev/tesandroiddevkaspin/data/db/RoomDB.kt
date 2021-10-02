package com.yudev.tesandroiddevkaspin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yudev.tesandroiddevkaspin.data.model.MenuBarang
import com.yudev.tesandroiddevkaspin.data.model.TransaksiBarang

@Database(entities = [MenuBarang::class, TransaksiBarang::class], version = 1)
abstract class RoomDB : RoomDatabase() {
    abstract fun getMenuBarang(): MenuBarangDao
    abstract fun getTransaksi(): TransaksiDao

    companion object {
        @Volatile
        var instance: RoomDB? = null

        fun getDatabase(context: Context): RoomDB? {
            if (instance == null) {
                synchronized(RoomDB::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            RoomDB::class.java, "kasir.db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance
        }

    }
}