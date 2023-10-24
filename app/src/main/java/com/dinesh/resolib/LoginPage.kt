package com.dinesh.resolib

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_page.*


class LoginPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var chkBoxRememberMe: CheckBox
    val SHARED_PREFS = "sharedPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()
        chkBoxRememberMe = checkBox_login

        ToRegisterLink.setOnClickListener {
            val intent = Intent(this@LoginPage, Registration::class.java)
            startActivity(intent)
        }


        Login.setOnClickListener {

            when {
                (LoginEmail.text.toString().trim { it <= ' ' }.isEmpty()) -> {
                    LoginEmail.error = "Email is Required."
                }
                (LoginPassword.text.toString().trim { it <= ' ' }.isEmpty()) -> {
                    LoginPassword.error = "Password is Required."
                }
                else -> {
                    val email: String = LoginEmail.text.toString().trim { it <= ' ' }
                    val password: String = LoginPassword.text.toString().trim { it <= ' ' }
                    LoginprogressBar.setVisibility(View.VISIBLE)
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("email", "true");
                            editor.apply();

                            Toast.makeText(
                                this@LoginPage,
                                "You have logged in successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            val intent = Intent(this@LoginPage, DashboardCardView::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id", auth.currentUser!!.uid)
                            intent.putExtra("email", email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginPage,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            LoginprogressBar.setVisibility(View.GONE)
                        }
                    }
                }
            }
        }

        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }
    }
}
