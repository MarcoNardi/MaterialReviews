package com.example.materialreviews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.materialreviews.db.AppDatabase
import com.example.materialreviews.db.UserEntity
import com.example.materialreviews.db.UserViewModel
import com.example.materialreviews.db.UserViewModelFactory
import com.example.materialreviews.ui.theme.MaterialReviewsTheme

class MainActivity : ComponentActivity() {

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    public val userModel: UserViewModel by viewModels{
        UserViewModelFactory(
            database.userDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)


        setContent {
            MaterialReviewsTheme {
                MyApp()
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun MyApp(){
        var firstname by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }
        // A surface container using the 'background' color from the theme

        Column (modifier = Modifier.fillMaxSize()){

            firstNameField(firstname, {firstname=it})
            firstNameField(firstName = lastname, onNameChange = {lastname=it})
            Button(onClick = { userModel.addUser(0, firstName = firstname, lastName = lastname) }) {
                Text(text="aggiungi al database")
            }
            userList(usersData = userModel.getAllUsers().observeAsState())

        }

    }
}

@Composable
fun userList(usersData: State<List<UserEntity>?>){
    val data: List<UserEntity>? = usersData.value
    if(data!=null){
        LazyColumn {
            items(data) { user ->
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
fun firstNameField(firstName: String, onNameChange: (String) -> Unit){
    TextField(
        value = firstName,
        onValueChange = onNameChange,
        label = { Text("First name:") }
    )
}