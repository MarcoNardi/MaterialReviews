package com.example.materialreviews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.materialreviews.db.*
import com.example.materialreviews.ui.theme.MaterialReviewsTheme
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    public val userModel: UserViewModel by viewModels{
        UserViewModelFactory(
            database.userDao()
        )
    }
    public val restaurantModel: RestaurantViewModel by viewModels{
        RestaurantViewModelFactory(
            database.restaurantDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)


        setContent {
            MaterialReviewsTheme {
                RestaurantTest()
                //UserTest()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun RestaurantTest(){
        var name by remember { mutableStateOf("") }
        var sito by remember { mutableStateOf("") }
        var orario by remember { mutableStateOf("") }
        var citta by remember { mutableStateOf("") }
        var via by remember { mutableStateOf("") }
        var num_civico by remember { mutableStateOf("0") }
        Column{
            nameField(name, {name=it})
            nameField(sito, {sito=it})
            nameField(orario, {orario=it})
            nameField(citta, {citta=it})
            nameField(via, {via=it})
            numberField(num_civico, {num_civico=it})
            Button(onClick = { restaurantModel.addRestaurant(0, name, sito, citta, via, num_civico.toInt(), orario) }) {
                Text(text="aggiungi al database")
            }
            RestaurantList(restaurantData = restaurantModel.getAllRestaurants().observeAsState())
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun UserTest(){
        var firstname by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }

        Column (modifier = Modifier.fillMaxSize()){

            nameField(firstname, {firstname=it})
            nameField(firstName = lastname, onNameChange = {lastname=it})
            Button(onClick = { userModel.addUser(0, firstName = firstname, lastName = lastname) }) {
                Text(text="aggiungi al database")
            }
            userList(usersData = userModel.getAllUsers().observeAsState())

        }

    }
}

@Composable
fun RestaurantList(restaurantData: State<List<RestaurantEntity>?>){
    val data by restaurantData
    if(data!=null){
        LazyColumn {
            items(data!!) { user ->
                Text(text=user.toString())
            }
        }
    }
}


@Composable
fun userList(usersData: State<List<UserEntity>?>){
    val data by usersData
    if(data!=null){
        LazyColumn {
            items(data!!) { user ->
                Text(text=user.toString())
            }
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun DefaultPreview() {
    MaterialReviewsTheme {
        Greeting("Android")
    }
}

@Composable
fun nameField(firstName: String, onNameChange: (String) -> Unit){
    TextField(
        value = firstName,
        onValueChange = onNameChange,
        label = { Text("First name:") }
    )
}
@Composable
fun numberField(name: String, onNameChange: (String) -> Unit){
    TextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("number") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

}