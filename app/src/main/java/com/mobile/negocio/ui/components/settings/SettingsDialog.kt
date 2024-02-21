package com.mobile.negocio.ui.components.settings

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.negocio.R
import com.mobile.negocio.data.debt.Debt
import com.mobile.negocio.data.income.Income
import com.mobile.negocio.ui.AppViewModelProvider
import com.mobile.negocio.ui.theme.AppTheme
import com.mobile.negocio.ui.views.RegisterViewModel
import com.mobile.negocio.ui.views.RegisterViewModelAlt
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun SettingsDialog(onDismiss: () -> Unit) {
    Settings(
        onDismiss = onDismiss,
    )
}

@Composable
fun Settings(
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Divider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsPanel()
                Divider(Modifier.padding(top = 8.dp))
                LinksPanel()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.dismiss_dialog_button_text),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}
@Composable
private fun ColumnScope.SettingsPanel(
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelALt: RegisterViewModelAlt = viewModel(factory = AppViewModelProvider.Factory),
) {
    val registerUiState by viewModel.registerUiState.collectAsState()
    val registerUiStateAlt by viewModelALt.registerUiStateAlt.collectAsState()
    val context = LocalContext.current

    Button(
        onClick = {
            onExport(
                registerUiState.itemList,
                registerUiStateAlt.itemList,
                context
            )
        },
        modifier = Modifier
            .align(Alignment.Start)
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CloudDownload, contentDescription = "Export data")
            Text(text = " Exportar base de dados", modifier = Modifier.padding(start = 4.dp))
        }
    }
    Text(text = "Exportar dados para formato .CSV", modifier = Modifier.padding(8.dp))
}

fun onExport(income: List<Income>, debt: List<Debt>, current: Context) {

    val income_content = StringBuilder()
    income_content.append("ID, Nome, Contacto, Nr.Favos, Valor, Data\n")
    income.forEach { data ->
        income_content.append("${data.id},${data.name},${data.contact},${data.quantity},${data.value}, ${data.date}\n")
    }

    val debt_content = StringBuilder()
    debt_content.append("ID, Fornecedor, Valor, Detalhes, Data\n")
    debt.forEach { data ->
        debt_content.append("${data.id},${data.name},${data.value},${data.details}, ${data.date}\n")
    }

    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val file = "Dados_exportados_${formatter.format(currentDateTime)}.csv"

    val resolver = current.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
    }

    val contentUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val uri = resolver.insert(contentUri, contentValues)

    uri?.let { fileUri ->
        resolver.openOutputStream(fileUri)?.use { outputStream ->
            writeCsvData(outputStream, income_content.toString(), debt_content.toString())
            resolver.notifyChange(fileUri, null)
        }
    }
}

private fun writeCsvData(outputStream: OutputStream, incomeData: String, debtData: String) {
    BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
        writer.write("Receitas:\n$incomeData\n\n Despesas:\n$debtData")
    }
}

@Composable
private fun LinksPanel() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val uriHandler = LocalUriHandler.current
        TextButton(
            onClick = { uriHandler.openUri(LICENSE_URL) }
        ) {
            Text(text = stringResource(R.string.licenses))
        }
        TextButton(
            onClick = { uriHandler.openUri(DEV_URL) },
        ) {
            Text(text = stringResource(R.string.dev))
        }
    }
}


@Preview
@Composable
fun SettingsPreview(){
    AppTheme {
        Settings(
            onDismiss = {}
        )
    }
}

private const val DEV_URL = "https://github.com/M1ngos/"
private const val LICENSE_URL = "https://en.wikipedia.org/wiki/Unlicense"
