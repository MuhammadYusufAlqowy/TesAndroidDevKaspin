package com.yudev.tesandroiddevkaspin.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.yudev.tesandroiddevkaspin.data.INTEN_VALUE
import com.yudev.tesandroiddevkaspin.data.db.RoomDB
import com.yudev.tesandroiddevkaspin.data.model.MenuBarang
import com.yudev.tesandroiddevkaspin.data.myutil.checkInput
import com.yudev.tesandroiddevkaspin.data.myutil.snack
import com.yudev.tesandroiddevkaspin.databinding.ActivityTambahBarangBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class TambahBarangActivity : BaseActivity<ActivityTambahBarangBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = RoomDB.getDatabase(this)!!
        val barang = intent.getParcelableExtra<MenuBarang>(INTEN_VALUE)
        layout.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            if (barang == null){
                db.getMenuBarang().select().observe(this@TambahBarangActivity){ list->
                    list?.let {
                        val data = it.maxByOrNull { i -> i.id_barang!! }
                        if (data != null){
                            tilKodeBarang.editText?.setText("KB-"+data?.kode_barang?.removePrefix("KB-").toInt().plus(1).toString())
                        }else{
                            tilKodeBarang.editText?.setText("KB-100")
                        }
                    }

                }
            }else{
                tilKodeBarang.editText?.setText(barang?.kode_barang)
                tilNamaBarang.editText?.setText(barang?.nama_barang)
                tilStok.editText?.setText(barang?.stok.toString())
            }

            btnSimpan.setOnClickListener {
                if (checkInput(tilKodeBarang) && checkInput(tilNamaBarang) && checkInput(tilStok)){
                    var newBarang = barang ?: MenuBarang(kode_barang = "",nama_barang = "",stok = 1)
                    newBarang.nama_barang = tilNamaBarang.editText?.text.toString()
                    newBarang.stok = tilStok.editText?.text.toString().toInt()
                    newBarang.kode_barang = tilKodeBarang.editText?.text.toString()
                    btnSimpan.isEnabled = false
                    CoroutineScope(Main).launch {
                        val simpan = db.getMenuBarang().insert(newBarang)
                        if (simpan > 0){
                            root.snack("Data berhasil disimpan!", btnSimpan)
                            if (barang==null){
                                tilNamaBarang.editText?.setText("")
                                tilStok.editText?.setText("")
                                tilNamaBarang.editText?.clearFocus()
                                tilStok.editText?.clearFocus()
                            }else{
                                tilNamaBarang.editText?.clearFocus()
                                tilStok.editText?.clearFocus()
                            }

                        }else{
                            root.snack("Data gagal disimpan!", btnSimpan)
                        }
                        btnSimpan.isEnabled = true
                    }
                }
            }

        }
    }

    override fun getBindingView(): ActivityTambahBarangBinding {
        return ActivityTambahBarangBinding.inflate(layoutInflater)
    }
}