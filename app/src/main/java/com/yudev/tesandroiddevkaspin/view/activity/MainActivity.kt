package com.yudev.tesandroiddevkaspin.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.yudev.tesandroiddevkaspin.R
import com.yudev.tesandroiddevkaspin.data.INTEN_VALUE
import com.yudev.tesandroiddevkaspin.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
        layout.apply {
            val inten = Intent(this@MainActivity,DaftarBarangActivity::class.java)
            btnBarang.setOnClickListener {
                inten.putExtra(INTEN_VALUE,0)
                startActivity(inten)
            }

            btnTransaksi.setOnClickListener {
                inten.putExtra(INTEN_VALUE,1)
                startActivity(inten)
            }
        }
    }

    override fun getBindingView(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}