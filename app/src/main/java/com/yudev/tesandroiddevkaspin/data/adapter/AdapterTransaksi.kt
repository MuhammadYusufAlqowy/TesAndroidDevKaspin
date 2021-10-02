package com.yudev.tesandroiddevkaspin.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yudev.tesandroiddevkaspin.data.INTEN_VALUE
import com.yudev.tesandroiddevkaspin.data.model.MenuBarang
import com.yudev.tesandroiddevkaspin.data.model.TransaksiBarang
import com.yudev.tesandroiddevkaspin.databinding.LayoutListBarangBinding
import com.yudev.tesandroiddevkaspin.view.activity.TambahBarangActivity

class AdapterTransaksi(listItem: MutableList<TransaksiBarang>) : BaseRecyclerViewAdapter<TransaksiBarang, LayoutListBarangBinding>(listItem) {
    var hapusBarang:((TransaksiBarang) -> Unit)? = null
    override fun setViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): LayoutListBarangBinding {
        return LayoutListBarangBinding.inflate(layoutInflater,parent,false)
    }

    override fun bind(item: TransaksiBarang, view: LayoutListBarangBinding, position: Int) {
        view.apply {
            tvNama.text = item.nama_barang
            tvKode.text = item.kode_barang
            tvStok.text = "Jumlah: ${item.jumlah}"
            btnDelete.visibility = View.VISIBLE
            btnEdit.visibility = View.GONE
            btnAddCart.visibility = View.GONE

            btnDelete.setOnClickListener {
                hapusBarang?.let {
                    it(item)
                }
            }


        }
    }

    fun delete(listener:(TransaksiBarang) -> Unit){
        hapusBarang = listener
    }

}