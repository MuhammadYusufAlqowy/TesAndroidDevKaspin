package com.yudev.tesandroiddevkaspin.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.yudev.tesandroiddevkaspin.data.INTEN_KEY
import com.yudev.tesandroiddevkaspin.data.INTEN_VALUE
import com.yudev.tesandroiddevkaspin.data.model.OrderBarang
import com.yudev.tesandroiddevkaspin.databinding.LayoutListOrderBinding
import com.yudev.tesandroiddevkaspin.view.activity.ChekoutActivity

class AdapterOrder(listItem: MutableList<OrderBarang>) : BaseRecyclerViewAdapter<OrderBarang, LayoutListOrderBinding>(listItem) {

    var hapusBarang:((OrderBarang) -> Unit)? = null
    override fun setViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): LayoutListOrderBinding {
        return LayoutListOrderBinding.inflate(layoutInflater,parent,false)
    }

    override fun bind(item: OrderBarang, view: LayoutListOrderBinding, position: Int) {
        view.apply {
            tvNama.text = item.nama_order
            tvKode.text = item.kode_order
            btnEdit.setOnClickListener {
                root.context.apply {
                    val intent=Intent(this,ChekoutActivity::class.java)
                    intent.putExtra(INTEN_KEY,item.kode_order)
                    startActivity(intent)
                }
            }

            btnDelete.setOnClickListener {
                hapusBarang?.let {
                    it(item)
                }
            }

        }
    }

    fun delete(listener:(OrderBarang) -> Unit){
        hapusBarang = listener
    }

}