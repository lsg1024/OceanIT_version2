package com.example.oceanit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var viewPager2 : ViewPager2
    lateinit var tabLayout: TabLayout
    var wait:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.viewpage2)
        tabLayout = findViewById(R.id.tabLayout)

        viewPager2.apply {
            adapter = FragmentAdapter(context as FragmentActivity)

            isUserInputEnabled = false
        }

        TabLayoutMediator(tabLayout, viewPager2) {tab, position ->
            when (position) {
                0 -> {
                    tab.text = "홈"
                    tab.setIcon(R.drawable.airplay)

                }

                1 -> {
                    tab.text = "그래프"
                    tab.setIcon(R.drawable.combo_chart)
                }

                2 -> {
                    tab.setIcon(R.drawable.set)
                    tab.text = "셋팅"
                }
            }
        }.attach()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - wait >= 2000) {
            wait = System.currentTimeMillis()
            Snackbar.make(viewPager2, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Snackbar.LENGTH_LONG).show()
        } else {
            finish()
        }
    }
}