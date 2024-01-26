package com.mobile.negocio.ui

import android.text.Spannable.Factory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mobile.negocio.ui.entries.RegistryEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
//        initializer {
////            RegistryEntryViewModel()
//        }
    }
}

