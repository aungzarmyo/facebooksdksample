package com.example.aungzarmyo.facebooksdksample

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.applinks.AppLinkData
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity() {

    val callbackManager = CallbackManager.Factory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printKeyHash()

        AppLinkData.fetchDeferredAppLinkData(
            this) {
            // Process app link data
                appLinkData ->
            if (appLinkData != null) {
                if (BuildConfig.DEBUG)
                    Log.i("Deep link is", appLinkData.targetUri.toString())

                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        login_button.setReadPermissions(Arrays.asList("email"))
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                val accessToken = loginResult.accessToken
                Log.i("Access Token", accessToken.token)
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            }

            override fun onCancel() {
                // App code
                Toast.makeText(this@MainActivity, "Cancel", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                // App code
                Toast.makeText(this@MainActivity, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun printKeyHash() {
        // Add code to print out the key hash
        try {
            val info =
                packageManager.getPackageInfo("com.example.aungzarmyo.facebooksdksample", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("KeyHash:", e.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("KeyHash:", e.toString())
        }

    }

}
