package com.yudev.tesandroiddevkaspin.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.yudev.tesandroiddevkaspin.data.INTEN_KEY
import com.yudev.tesandroiddevkaspin.data.adapter.AdapterTransaksi
import com.yudev.tesandroiddevkaspin.data.db.RoomDB
import com.yudev.tesandroiddevkaspin.data.model.OrderBarang
import com.yudev.tesandroiddevkaspin.data.model.TransaksiBarang
import com.yudev.tesandroiddevkaspin.data.myutil.snack
import com.yudev.tesandroiddevkaspin.databinding.ActivityChekoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ChekoutActivity : BaseActivity<ActivityChekoutBinding>() {
    val ff = FirebaseFirestore.getInstance().collection("order")
    var transaksi = mutableListOf<TransaksiBarang>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val order = intent.getStringExtra(INTEN_KEY)
        val db = RoomDB.getDatabase(this)!!

        layout.apply {
            toolbar.setNavigationOnClickListener { onBackPressed() }
            if (order.isNullOrEmpty()) {
                btnSaveOrder.setText("Save Order")
                db.getTransaksi().select().observe(this@ChekoutActivity) {
                    it?.let {
                        transaksi = it.toMutableList()
                        rclvBarang.apply {
                            adapter = AdapterTransaksi(it.toMutableList()).also {
                                it.delete { item ->
                                    val dialog = MaterialAlertDialogBuilder(this@ChekoutActivity)
                                    dialog.setMessage("Yakin ingin menghapus ${item.nama_barang}?")
                                    dialog.setPositiveButton("Ya") { d, i ->
                                        loading.show()
                                        CoroutineScope(Main).launch {
                                            db.getTransaksi().delete(item)
                                            d.dismiss()
                                            loading.dismiss(500)
                                            root.snack("Barang berhasil dihapus!",btnSubmit)
                                        }
                                    }
                                    dialog.setNegativeButton("Tidak") { d, i ->
                                        d.dismiss()
                                    }
                                    dialog.show()

                                }
                            }
                            layoutManager = LinearLayoutManager(this@ChekoutActivity)
                            setHasFixedSize(true)
                        }
                    }
                }
            } else {
                btnSaveOrder.setText("Update Order")
                loading.show()
                ff.document(order).get().addOnSuccessListener { it ->
                    if (it!!.exists()) {
                        val list_order: List<HashMap<String, Any>> = it["list_order"] as List<HashMap<String, Any>>
                        val order_list = mutableListOf<TransaksiBarang>()
                        val order = OrderBarang(
                            kode_order = it["kode_order"].toString(),
                            nama_order = it["nama_order"].toString(),
                            list_order = mutableListOf()
                        )
                        list_order.forEach { map ->
                            order_list.add(
                                TransaksiBarang(
                                    id_transaksi = map["id_transaksi"].toString().toInt(),
                                    nama_barang = map["nama_barang"].toString(),
                                    jumlah = map["jumlah"].toString().toInt(),
                                    kode_barang = map["kode_barang"].toString(),
                                )
                            )
                        }
                        loading.dismiss(500)
                        transaksi = order_list
                        rclvBarang.apply {
                            adapter = AdapterTransaksi(transaksi).also {
                                it.delete { item ->
                                    val dialog = MaterialAlertDialogBuilder(this@ChekoutActivity)
                                    dialog.setMessage("Yakin ingin menghapus ${item.nama_barang}?")
                                    dialog.setPositiveButton("Ya") { d, i ->
                                        it.listItem.remove(item)
                                        transaksi = it.listItem
                                        it.notifyDataSetChanged()
                                        root.snack("Barang berhasil dihapus!", btnSubmit)
                                        d.dismiss()
                                    }
                                    dialog.setNegativeButton("Tidak") { d, i ->
                                        d.dismiss()
                                    }
                                    dialog.show()

                                }
                            }
                            layoutManager = LinearLayoutManager(this@ChekoutActivity)
                            setHasFixedSize(true)
                        }
                    }
                }
            }


            btnSaveOrder.setOnClickListener {
                if (order.isNullOrEmpty()) {
                    val dialog = MaterialAlertDialogBuilder(this@ChekoutActivity)
                    dialog.setMessage("Save order?")
                    dialog.setPositiveButton("Ya") { d, i ->
                        loading.show()
                        ff.get().addOnSuccessListener { l ->
                            var kode = ""
                            if (l.documents.isEmpty()) {
                                kode = "100"
                            } else {
                                kode =
                                    l.documents.last()["kode_order"].toString().removePrefix("KO-")
                                        .toInt().plus(1).toString()
                            }
                            val data = OrderBarang(
                                kode_order = "KO-$kode",
                                nama_order = "Order $kode",
                                list_order = transaksi.toMutableList()
                            )
                            ff.document("KO-$kode").set(data).addOnSuccessListener {
                                root.snack("Order berhasil disimpan!", btnSubmit)
                                d.dismiss()
                                loading.dismiss()
                                CoroutineScope(Main).launch {
                                    db.getTransaksi().deleteAll()
                                    startActivity(Intent(this@ChekoutActivity,DaftarTransaksiActivity::class.java))
                                    finish()
                                }
                            }
                        }

                    }
                    dialog.setNegativeButton("Tidak") { d, i ->
                        d.dismiss()
                    }
                    dialog.show()
                } else {
                    val dialog = MaterialAlertDialogBuilder(this@ChekoutActivity)
                    dialog.setMessage("Update order?")
                    dialog.setPositiveButton("Ya") { d, i ->
                        loading.show()
                        ff.document(order).update("list_order",transaksi).addOnSuccessListener {
                            loading.dismiss(500)
                            d.dismiss()
                            root.snack("Order berhasil diupdate!", btnSubmit)
                        }
                    }
                    dialog.setNegativeButton("Tidak") { d, i ->
                        d.dismiss()
                    }
                    dialog.show()
                }
            }

            btnCancel.setOnClickListener {
                onBackPressed()
            }

            btnSubmit.setOnClickListener {
                transaksi.forEach {
                    CoroutineScope(Main).launch {
                        val barang = db.getMenuBarang().selectBarangByKode(it.kode_barang)
                        val newBarang = barang
                        newBarang.stok = barang.stok-it.jumlah
                        db.getMenuBarang().insert(newBarang)
                    }
                }

                if (order.isNullOrEmpty()){
                    CoroutineScope(Main).launch {
                        db.getTransaksi().deleteAll()
                        startActivity(Intent(this@ChekoutActivity,SuccesActivity::class.java))
                        finish()
                    }
                }else{
                    loading.show()
                    ff.document(order).delete().addOnSuccessListener {
                        loading.dismiss()
                        startActivity(Intent(this@ChekoutActivity,SuccesActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    override fun getBindingView(): ActivityChekoutBinding {
        return ActivityChekoutBinding.inflate(layoutInflater)
    }
}