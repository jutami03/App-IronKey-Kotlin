package br.com.julianatami.ironkey


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.health.connect.datatypes.units.Length
import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
                    onValueChange = {
                        if (it.length <= maxCharacters)
                        generatedPassword = it},
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxSize(),
                    leadingIcon = { Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Icone cadeado"
                    ) },
                    trailingIcon = { Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copiar senha",
                        modifier = Modifier.clickable {
                            copyPassword(
                                context,
                                generatedPassword
                            )
                        }
                    ) }
                )

                Text("${generatedPassword.length} / $maxCharacters",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text("Tipo de senha")

                Row (verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Row (verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)){
                        RadioButton(selected = isPin, onClick = {isPin = true})
                        Text("Pin")
                    }

                    Row (verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)){
                        RadioButton(selected = !isPin, onClick = {isPin = false})
                        Text("Senha padrão")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Button(modifier = Modifier
                    .fillMaxWidth(),
                    onClick = {
                        val generator = if (isPin){
                            PinPasswordGenerator()
                        }else{
                            StandardPasswordGenerator()
                        }

                        generatedPassword = generator.generate(maxCharacters)
                    }) {
                    Text("Gerar Senha")
                }
            }
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



