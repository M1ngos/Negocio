package com.mobile.negocio.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mobile.negocio.NegocioApplication
import com.mobile.negocio.ui.entries.debt.RegistryEntryScreenAlt
import com.mobile.negocio.ui.entries.debt.RegistyEntryViewModelAlt
import com.mobile.negocio.ui.entries.income.RegistryEntryViewModel
import com.mobile.negocio.ui.views.RegisterViewModel
import com.mobile.negocio.ui.views.RegisterViewModelAlt

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            RegistryEntryViewModel(negocioApplication().container.itemsRepository)
        }

        initializer {
            RegisterViewModel(negocioApplication().container.itemsRepository)
        }

        initializer {
            RegistyEntryViewModelAlt(negocioApplication().container.itemsRepositoryAlt)
        }

        initializer {
            RegisterViewModelAlt(negocioApplication().container.itemsRepositoryAlt)
        }
    }
}
fun CreationExtras.negocioApplication(): NegocioApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NegocioApplication)