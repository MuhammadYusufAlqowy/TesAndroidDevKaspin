package com.yudev.tesandroiddevkaspin.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yudev.tesandroiddevkaspin.R
import com.yudev.tesandroiddevkaspin.data.INTEN_VALUE
import com.yudev.tesandroiddevkaspin.data.adapter.AdapterBarang
import com.yudev.tesandroiddevkaspin.data.db.RoomDB
import com.yudev.tesandroiddevkaspin.data.model.MenuBarang
import com.yudev.tesandroiddevkaspin.data.myutil.snack
import com.yudev.tesandroiddevkaspin.databinding.ActivityDaftarBarangBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class DaftarBarangActivity : BaseActivity<ActivityDaftarBarangBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = RoomDB.getDatabase(this)!!

        val type = intent.getIntExtra(INTEN_VALUE, 0)
        layout.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
            if (type == 0) {
                toolbar.title = "Barang"
                btnAction.text = "Tambah Barang"
                btnAction.setIconResource(R.drawable.icons8_plus_math)
                db.getMenuBarang().select().observe(this@DaftarBarangActivity){
                    it?.let{
                        rclvBarang.apply {
                            adapter = AdapterBarang(it.toMutableList()).also {
                                it.delete { item->
                                    val dialog = MaterialAlertDialogBuilder(this@DaftarBarangActivity)
                                    dialog.setMessage("Yakin ingin menghapus ${item.nama_barang}?")
                                    dialog.setPositiveButton("Ya"){ d,i->
                                        CoroutineScope(Main).launch {
                                            db.getMenuBarang().delete(item)
                                            d.dismiss()
                                            root.snack("Barang berhasil dihapus!")
                                        }
                                    }
                                    dialog.setNegativeButton("Tidak"){d,i->
                                        d.dismiss()
                                    }
                                    dialog.show()

                                }
                            }
                            layoutManager = LinearLayoutManager(this@DaftarBarangActivity)
                            setHasFixedSize(true)
                        }
                    }
                }

            } else {
                toolbar.title = "Transaksi"
                btnAction.setIconResource(R.drawable.icons8_checkmark)
                toolbar.inflateMenu(R.menu.transaksi)
                toolbar.setOnMenuItemClickListener {
                    startActivity(Intent(this@DaftarBarangActivity,DaftarTransaksiActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
                db.getMenuBarang().select().observe(this@DaftarBarangActivity){
                    it?.let{
                        rclvBarang.apply {
                            adapter = AdapterBarang(it.toMutableList(),true).also { ad->
                                ad.addCart { transaksiBarang ->
                                    CoroutineScope(Main).launch {
                                        val item = db.getTransaksi().selectByKodeBarang(transaksiBarang.kode_barang)
                                        if(item != null){
                                            item.jumlah++
                                            db.getTransaksi().insert(item)
                                        }else{
                                            db.getTransaksi().insert(transaksiBarang)
                                        }
                                    }
                                }
                            }
                            layoutManager = LinearLayoutManager(this@DaftarBarangActivity)
                            setHasFixedSize(true)
                        }
                    }
                }
                db.getTransaksi().select().observe(this@DaftarBarangActivity) { list->
                    btnAction.text = "Chekout (${list?.sumOf { it.jumlah }})"
                }
            }


            btnAction.setOnClickListener {
                if (btnAction.text == "Tambah Barang") {
                   startActivity(Intent(this@DaftarBarangActivity,TambahBarangActivity::class.java))
                }else{
                    startActivity(Intent(this@DaftarBarangActivity,ChekoutActivity::class.java))
                }
            }
        }

    }

    override fun getBindingView(): ActivityDaftarBarangBinding {
        return ActivityDaftarBarangBinding.inflate(layoutInflater)
    }
}