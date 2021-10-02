package com.yudev.tesandroiddevkaspin.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.yudev.tesandroiddevkaspin.R
import com.yudev.tesandroiddevkaspin.data.INTEN_VALUE
import com.yudev.tesandroiddevkaspin.databinding.ActivitySuccesBinding

class SuccesActivity : BaseActivity<ActivitySuccesBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        layout.apply {
            btnHome.setOnClickListener {
                startActivity(Intent(this@SuccesActivity,MainActivity::class.java))
                finishAffinity()
            }

            btnNewTransaction.setOnClickListener {
                val inten = Intent(this@SuccesActivity,DaftarTransaksiActivity::class.java)
                inten.putExtra(INTEN_VALUE,1)
                startActivity(inten)
                finishAffinity()
            }
        }
    }

    override fun getBindingView(): ActivitySuccesBinding {
        return ActivitySuccesBinding.inflate(layoutInflater)
    }
}