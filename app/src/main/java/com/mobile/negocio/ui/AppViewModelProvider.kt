package com.mobile.negocio.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mobile.negocio.NegocioApplication
import com.mobile.negocio.ui.entries.RegistryEntryViewModel
import com.mobile.negocio.ui.views.RegisterViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            RegistryEntryViewModel(negocioApplication().container.itemsRepository)
        }

        initializer {
            RegisterViewModel(negocioApplication().container.itemsRepository)
        }
    }
}
fun CreationExtras.negocioApplication(): NegocioApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NegocioApplication)
