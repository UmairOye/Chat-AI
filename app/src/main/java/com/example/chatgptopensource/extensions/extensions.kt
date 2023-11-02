package com.example.chatgptopensource.extensions

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String)
{
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}