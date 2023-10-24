package com.dinesh.resolib

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dashboard_card_view.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.coroutines.NonCancellable.start

class DashboardCardView : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var floatingActionButton: FloatingActionButton
    private lateinit var db: FirebaseFirestore
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_card_view)
        val drawerLayout: DrawerLayout = drawer_layout
        val navView: NavigationView = nav_view

        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)?:return
        val isLogin = sharedPref.getString("Email","1")
//        val email = intent.getStringExtra("email")
        if(isLogin=="1"){
            var email = intent.getStringExtra("email")
            if(email!=null){
                setText(email)
                with(sharedPref.edit()){
                    putString("email",email)
                    apply()
                }
            }
            else{
                var intent = Intent(this,LoginPage::class.java)
                startActivity(intent)
                finish()
            }
        }
        else{
            setText(isLogin)
        }


        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home ->{ Toast.makeText(this@DashboardCardView,"Clicked Home",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@DashboardCardView, DashboardCardView::class.java)
                    startActivity(intent)
                }
                R.id.nav_contact -> {Toast.makeText(this@DashboardCardView,"Clicked Contacts",Toast.LENGTH_SHORT).show()
                }
                R.id.nav_file -> {
                    Toast.makeText(this@DashboardCardView, "Clicked Files", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@DashboardCardView, MainActivity::class.java))
                }
                R.id.nav_about -> Toast.makeText(this@DashboardCardView,"Clicked About",Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> {Toast.makeText(this@DashboardCardView,"Clicked Log Out",Toast.LENGTH_SHORT).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@DashboardCardView, LoginPage::class.java))
                    finish()
                }
                R.id.nav_share -> Toast.makeText(this@DashboardCardView,"Clicked Share",Toast.LENGTH_SHORT).show()
                R.id.nav_rateus-> Toast.makeText(this@DashboardCardView,"Clicked Rate Us",Toast.LENGTH_SHORT).show()
            }
            true
        }
        floatingActionButton = float_btn
        floatingActionButton.setOnClickListener(View.OnClickListener {
            Toast.makeText(this@DashboardCardView,"Clicked on circular button",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, uploadfile::class.java)
            //intent.putExtra("userName", user_name)
            startActivity(intent)
        })
        cardBtech.setOnClickListener {
            startActivity(Intent(this,BTech_pdfs_view::class.java))
        }
        cardBDes.setOnClickListener {
            startActivity(Intent(this,BDes_pdfs_view::class.java))
        }
        cardBBA.setOnClickListener {
            startActivity(Intent(this,BBA_pdfs_view::class.java))
        }
        cardMasters.setOnClickListener {
            startActivity(Intent(this,Master_pdfs_view::class.java))
        }
    }
    private fun setText(email:String?){
        db = FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("USERS").document(email).get()
                .addOnSuccessListener { tasks->
                    val user_name =tasks.get("Name").toString()
                    Name_user.text= user_name
                    email_id.text = tasks.get("email").toString()
                }
        }
    }
}