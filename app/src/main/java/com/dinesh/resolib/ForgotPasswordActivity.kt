package com.dinesh.resolib

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPasswordActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        //val email: String = editEmailAddress.text.toString().trim { it <= ' ' }
        btn_submit.setOnClickListener{
            val email: String = editEmailAddress.text.toString().trim { it <= ' ' }
            if(email.isEmpty()){
                Toast.makeText(this,"Please enter email address",Toast.LENGTH_SHORT).show()

            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Email sent successfully to reset your password!",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else{
                            Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }
}