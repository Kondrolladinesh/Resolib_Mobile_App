package com.dinesh.resolib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*


class Registration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        ToLoginLink.setOnClickListener {
            val intent = Intent(this@Registration, LoginPage::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            when {
                (RegisterEmail.text.toString().trim { it <= ' ' }.isEmpty()) -> {
                    RegisterEmail.error = "Email is Required."
                }
                (RegisterPassword.text.toString().trim { it <= ' ' }.isEmpty()) -> {
                    RegisterPassword.error = "Password is Required."
                }
                (RegisterPassword.length() < 6) -> {
                    RegisterPassword.error = "Password Must be >= 6 Characters"
                }

                else -> {
                    val email = RegisterEmail.text.toString().trim { it <= ' ' }
                    val password = RegisterPassword.text.toString().trim { it <= ' ' }
                    val name = FullName.text.toString()
                    val phone = Phone.text.toString()

                    val user = hashMapOf(
                        "Name" to name,
                        "Phone" to phone,
                        "email" to email
                    )
                    val Users = db.collection("USERS")
                    val query = Users.whereEqualTo("email", email).get()
                        .addOnSuccessListener { it ->
                            if (it.isEmpty) {
                                RegisterprogressBar.setVisibility(View.VISIBLE)
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Users.document(email).set(user)
                                            val firebaseUser: FirebaseUser = task.result!!.user!!
                                            Toast.makeText(
                                                this@Registration,
                                                "You have Registered Successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            val intent = Intent(
                                                this@Registration,
                                                DashboardCardView::class.java
                                            )
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            intent.putExtra("name", firebaseUser.uid)
                                            intent.putExtra("email_id", email)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@Registration,
                                                task.exception!!.message.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            RegisterprogressBar.setVisibility(View.GONE)
                                        }
                                    }

                            } else {
                                Toast.makeText(this, "User Already Registered", Toast.LENGTH_LONG)
                                    .show()
                                val intent = Intent(this, DashboardCardView::class.java)
                                startActivity(intent)
                            }
                        }

                }
            }

        }

    }
}

