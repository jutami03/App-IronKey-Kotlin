package br.com.julianatami.ironkey


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.transition.Slide
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.julianatami.ironkey.ui.theme.IronKeyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IronKeyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IronKeyForm(Modifier.padding(innerPadding))
                }
            }
        }
    }
}


fun copyPassword(context: Context, password: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as
                ClipboardManager
    val clip = ClipData.newPlainText("Senha", password)
    clipboardManager.setPrimaryClip(clip)
    Toast.makeText(context, "Senha copiada!",
        Toast.LENGTH_SHORT).show()
}

@Composable
fun IronKeyForm(modifier: Modifier = Modifier) {

    var generatedPassword by remember { mutableStateOf("") }
    var maxCharacters by remember { mutableIntStateOf(12) }
    var isPin by remember { mutableStateOf(false) }
    var isEditable by remember { mutableStateOf(false) }
    var passwordLength by remember { mutableStateOf(6f) }
    var includeUpperCase by remember { mutableStateOf(false) }
    var includeLowerCase by remember { mutableStateOf(false) }
    var includeNumbers by remember { mutableStateOf(false) }
    var includeSymbols by remember { mutableStateOf(false) }
    var passwordComplexity by remember { mutableStateOf(PasswordComplexity.MEDIUM) }

    val context = LocalContext.current


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.homem_de_ferro),
            contentDescription = "Logo do app",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground),
            contentScale = ContentScale.Crop
        )

        Text("Blindagem Total",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
        ) {
            Column {
                OutlinedTextField(
                    value = generatedPassword,
                    enabled = isEditable,
                    onValueChange = {
                        if (it.length <= maxCharacters)
                            generatedPassword = it
                    },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxSize(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Icone cadeado"
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "Copiar senha",
                            modifier = Modifier.clickable {
                                copyPassword(
                                    context,
                                    generatedPassword
                                )
                            }
                        )
                    }
                )

                Text(
                    "${generatedPassword.length} / $maxCharacters",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text("Tipo de senha")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(selected = isPin, onClick = { isPin = true })
                        Text("Pin")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(selected = !isPin, onClick = { isPin = false })
                        Text("Senha padrão")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = if (isEditable) Icons.Default.LockOpen
                        else Icons.Default.Lock,
                        contentDescription = "Ícone do cadeado"
                    )

                    Text(
                        "Permitir editar senha?",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Switch(
                        checked = isEditable,
                        //onCheckedChange = função que acontece toda vez que mudar
                        onCheckedChange = { isEditable = it }
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxSize()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isEditable) {
                    Text("Complexidade da senha")
                    PasswordComplexityDropdown(selectedComplexity = passwordComplexity) {
                        passwordComplexity = it
                        maxCharacters = it.length
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Tamanho da senha ${passwordLength.toInt()}")

                    Slider(
                        value = passwordLength,
                        onValueChange = { passwordLength = it },
                        valueRange = 4.toFloat()..12.toFloat(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Caracteres")
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = includeUpperCase,
                                onCheckedChange = { includeUpperCase = it }
                            )
                            Text("Maiúsculas")
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = includeLowerCase,
                                onCheckedChange = { includeLowerCase = it }
                            )
                            Text("Minúsculas")
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = includeNumbers,
                                onCheckedChange = { includeNumbers = it }
                            )
                            Text("Números")
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Checkbox(
                                checked = includeSymbols,
                                onCheckedChange = { includeSymbols = it }
                            )
                            Text("Símbolos")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(modifier = Modifier
            .fillMaxWidth(),
            onClick = {
                val generator = if (isPin){
                    PinPasswordGenerator()
                }else{
                    StandardPasswordGenerator(
                        includeUppercase = includeUpperCase,
                        includeLowercase = includeLowerCase,
                        includeNumbers = includeNumbers,
                        includeSymbols = includeSymbols
                    )
                }

                generatedPassword = generator.generate(maxCharacters)
            }) {
            Text("Gerar Senha")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun IronKeyFormPreview() {
    IronKeyTheme {
        IronKeyForm()
    }
}

interface PasswordGenerator {
    fun generate(length: Int):String
}

class PinPasswordGenerator : PasswordGenerator {
    override fun generate(length: Int): String {
        val digits = ('0'..'9')
        return (1..length)
            .map { digits.random() }
            .joinToString("")
    }
}

class StandardPasswordGenerator(
    private val includeUppercase: Boolean = true,
    private val includeLowercase: Boolean = true,
    private val includeNumbers: Boolean = true,
    private val includeSymbols: Boolean = true
) : PasswordGenerator {
    override fun generate(length: Int): String {
        val chars = buildList<Char> {
            if (includeUppercase) addAll('A'..'Z')
            if (includeLowercase) addAll('a'..'z')
            if (includeNumbers) addAll('0'..'9')
            if (includeSymbols)
                addAll("!@#\$%&*()_-+=<>?".toList())
        }
        if (chars.isEmpty()) return ""
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}

enum class PasswordComplexity(
    val title: String, val length: Int
){
    LOW("Baixo", 6),
    MEDIUM("Médio", 10),
    HIGH("Alto", 6)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordComplexityDropdown(
    selectedComplexity: PasswordComplexity,
    onComplexitySelected: (PasswordComplexity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Spacer(modifier = Modifier.height(20.dp))
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedComplexity.title,
            onValueChange = {},
            readOnly = true,
            label = { Text("Complexidade da senha") },
            trailingIcon = {

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PasswordComplexity.values().forEach { complexity ->
                DropdownMenuItem(
                    text = { Text(complexity.title) },
                    onClick = {
                        onComplexitySelected(complexity)
                        expanded = false
                    }
                )
            }
        }
    }
}



