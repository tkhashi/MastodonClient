package io.keiji.sample.mastodonclient.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.keiji.sample.mastodonclient.R

class LoginActivity : AppCompatActivity(R.layout.activity_login),
    LoginFragment.Callback{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null){
            val fragment = LoginFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, LoginFragment.TAG)
                .commit()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent ?: return

        val code = intent.data?.getQueryParameter("code") ?: return
        val loginFragment = supportFragmentManager.findFragmentByTag(LoginFragment.TAG)

        if (loginFragment is LoginFragment){
            loginFragment.requestAccessToken(code)
        }
    }

    override fun onAuthCompleted() {
        Toast.makeText(this, "ログイン完了。", Toast.LENGTH_LONG)
        setResult(Activity.RESULT_OK)
        finish()
    }


}