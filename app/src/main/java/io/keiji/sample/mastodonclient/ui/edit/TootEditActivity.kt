package io.keiji.sample.mastodonclient.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import io.keiji.sample.mastodonclient.R

class TootEditActivity : AppCompatActivity(),
        TootEditFragment.Callback{

    companion object{
        val TAG = TootEditActivity::class.java.simpleName

        fun newInstance(context: Context): Intent {
            val args = Bundle()
            return Intent(context, TootEditActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toot_edit)

        if (savedInstanceState == null){
            val fragment = TootEditFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, TootEditFragment.TAG)
                .commit()
        }
    }

    override fun onPostComplete() {
        setResult(Activity.RESULT_OK)
        finish()
    }
}