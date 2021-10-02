package com.yudev.tesandroiddevkaspin.view.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.yudev.tesandroiddevkaspin.data.adapter.AdapterOrder
import com.yudev.tesandroiddevkaspin.data.model.OrderBarang
import com.yudev.tesandroiddevkaspin.data.model.TransaksiBarang
import com.yudev.tesandroiddevkaspin.data.myutil.snack
import com.yudev.tesandroiddevkaspin.databinding.ActivityDaftarTransaksiBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Suppress("UNCHECKED_CAST")
class DaftarTransaksiActivity : BaseActivity<ActivityDaftarTransaksiBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()
        layout.apply {
            loading.show()
            db.collection("order")
                .addSnapshotListener { value, error ->
                if (!value!!.isEmpty){
                    val listOrder = mutableListOf<OrderBarang>()
                    value.documents.forEach {
                        val list_order: MutableList<TransaksiBarang> =
                            it["list_order"] as MutableList<TransaksiBarang>
                        val order = OrderBarang(
                            kode_order = it["kode_order"].toString(),
                            nama_order = it["nama_order"].toString(),
                            list_order = list_order
                        )
                        listOrder.add(order)
                        loading.dismiss(500)
                    }
                    rclvBarang.apply {
                        adapter = AdapterOrder(listOrder).also {
                            it.delete {
                                val dialog = MaterialAlertDialogBuilder(this@DaftarTransaksiActivity)
                                dialog.setMessage("Yakin ingin menghapus ${it.nama_order}?")
                                dialog.setPositiveButton("Ya"){ d,i->
                                    loading.show()
                                    db.collection("order").document(it.kode_order).delete().addOnSuccessListener {
                                        d.dismiss()
                                        loading.dismiss(500)
                                        root.snack("Barang berhasil dihapus!")
                                    }
                                }
                                dialog.setNegativeButton("Tidak"){d,i->
                                    d.dismiss()
                                }
                                dialog.show()
                            }
                        }
                        layoutManager = LinearLayoutManager(this@DaftarTransaksiActivity)
                        setHasFixedSize(true)
                    }
                }

            }
        }
    }

    override fun getBindingView(): ActivityDaftarTransaksiBinding {
        return ActivityDaftarTransaksiBinding.inflate(layoutInflater)
    }
}