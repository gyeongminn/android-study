package com.example.week9

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

var isHome : Boolean = true

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!isHome) return
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}

class HomeFragment : Fragment(R.layout.home_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            val viewModel: MyViewModel by activityViewModels()
            viewModel.increase()
            findNavController().navigate(R.id.action_homeFragment_to_nav1Fragment)
        }

        val viewModel: MyViewModel by activityViewModels()
        viewModel.myValue.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textView).text = viewModel.myValue.value.toString()
            isHome = false
        }
    }
}

class Nav1Fragment : Fragment(R.layout.nav1_frament) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            val viewModel: MyViewModel by activityViewModels()
            viewModel.increase()
            findNavController().navigate(R.id.action_nav1Fragment_to_nav2Fragment)
        }

        val viewModel: MyViewModel by activityViewModels()
        viewModel.myValue.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.textView).text = viewModel.myValue.value.toString()
        }
    }
}

class Nav2Fragment : Fragment(R.layout.nav2_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            isHome = true
            val viewModel: MyViewModel by activityViewModels()
            viewModel.increase()
            findNavController().navigate(R.id.action_nav2Fragment_to_homeFragment)
        }

        val viewModel: MyViewModel by activityViewModels()
        viewModel.myValue.observe(viewLifecycleOwner) {
            isHome = true
            view.findViewById<TextView>(R.id.textView).text = viewModel.myValue.value.toString()
        }
    }
}