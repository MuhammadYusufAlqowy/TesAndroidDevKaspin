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

class AdapterBarang(listItem: MutableList<MenuBarang>, var isTransaksi : Boolean = false) : BaseRecyclerViewAdapter<MenuBarang, LayoutListBarangBinding>(listItem) {
    var addCart:((TransaksiBarang) -> Unit)? = null
    var hapusBarang:((MenuBarang) -> Unit)? = null
    override fun setViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): LayoutListBarangBinding {
        return LayoutListBarangBinding.inflate(layoutInflater,parent,false)
    }

    override fun bind(item: MenuBarang, view: LayoutListBarangBinding, position: Int) {
        view.apply {
            tvNama.text = item.nama_barang
            tvKode.text = item.kode_barang
            tvStok.text = "Stok: ${item.stok}"
            btnEdit.setOnClickListener {
                root.context.apply {
                    val intent = Intent(this,TambahBarangActivity::class.java)
                    intent.putExtra(INTEN_VALUE,item)
                    startActivity(intent)
                }
            }

            btnDelete.setOnClickListener {
                hapusBarang?.let {
                    it(item)
                }
            }

            if (isTransaksi){
                tvStok.visibility = View.INVISIBLE
                btnDelete.visibility = View.GONE
                btnEdit.visibility = View.GONE
                btnAddCart.visibility = View.VISIBLE
                btnAddCart.setOnClickListener {
                    addCart?.let{
                        val transaksiBarang = TransaksiBarang(kode_barang = item.kode_barang,nama_barang = item.nama_barang, jumlah = 1)
                        it(transaksiBarang)
                    }
                }
            }
        }
    }

    fun addCart(listener:(TransaksiBarang) -> Unit){
        addCart = listener
    }


    fun delete(listener:(MenuBarang) -> Unit){
        hapusBarang = listener
    }

}