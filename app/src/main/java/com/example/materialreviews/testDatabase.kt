package com.example.materialreviews

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.materialreviews.db.*
import com.example.materialreviews.ui.theme.MaterialReviewsTheme

class TestActivity : ComponentActivity() {

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val userModel: UserViewModel by viewModels{
        UserViewModelFactory(
            database.userDao()
        )
    }
    val restaurantModel: RestaurantViewModel by viewModels{
        RestaurantViewModelFactory(
            database.restaurantDao()
        )
    }
    val imageModel: ImageViewModel by viewModels{
        ImageViewModelFactory(
            database.imageDao()
        )
    }

    val reviewModel: ReviewViewModel by viewModels{
        ReviewViewModelFactory(
            database.reviewDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)


        setContent {
            MaterialReviewsTheme {
                //reviewModel.addReview(4,1,1)
                //reviewModel.addReview(3,2,1)
                //reviewModel.addReview(1,2,2)

                /*
                val restaurantList= getInitialRestaurantsData()
                val userslist= getInitialUsersData()
                for(r in restaurantList){
                    restaurantModel.addRestaurant(r)
                }
                for (u in userslist){
                    userModel.addUser(u)
                }*/


                //ShowRestaurantWithImage()
                //ImagePicker()
                //RestaurantTest()
                //UserTest()
                reviewsTest()
            }
        }
    }

    @Composable
    fun reviewsTest(){
        Column{
            //reviewsList(restaurantModel.getRestaurantsWithReviews())
            reviewsOfUserList(reviewsData = userModel.getReviewsOfUser(2))
        }
    }

    @Composable
    fun ShowRestaurantWithImage(){
        Column (modifier = Modifier.padding(16.dp)){
            Text("BLALABLAb")
           // RestaurantWithImagesList(restaurantData = restaurantModel.getRestaurantsWithImage())
            RestaurantList(restaurantData =restaurantModel.getAllRestaurants().observeAsState(
                emptyList()))

        }


    }

    @Composable
    fun ImagePicker(){

        val imageUri = remember { mutableStateOf<Uri?>(null)}
        val imageData= remember {mutableStateOf<Bitmap?>(null)}
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri.value = it
        }
        var rid by remember{ mutableStateOf("")}
        Column{
            Button(onClick = {
                launcher.launch(
                    "image/*"
                )
            }){Text("pick an image")}
            numberField(rid, {rid=it})
            if(imageUri.value!=null){

                if (Build.VERSION.SDK_INT < 28) {
                    imageData.value = MediaStore.Images
                        .Media.getBitmap(LocalContext.current.contentResolver, imageUri.value)

                } else {
                    val dataSource =
                        ImageDecoder
                            .createSource(LocalContext.current.contentResolver, imageUri.value!!)

                    imageData.value = ImageDecoder.decodeBitmap(dataSource!!)
                }

            }
            if(imageData.value!=null){
                Image(bitmap = imageData.value!!.asImageBitmap() , contentDescription = null)
            }
            if(imageUri.value!=null && rid!="" && rid.toInt()>=1){
                Button(onClick = {imageModel.addImage(imageUri.value.toString(),rid.toInt())}) {
                    Text("add to database")
                }
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
private fun reviewsOfUserList(reviewsData: LiveData<UserWithReviews>) {
    val userWithReviews by reviewsData.observeAsState(null)
    if(userWithReviews!=null){
        val reviewList=userWithReviews!!.reviews
        LazyColumn(){
            items(reviewList){data->
                Text(text=data.toString())
            }
        }
    }

}
@Composable
fun <T> reviewsList(reviewsData: LiveData<List<T>>){
    val list by reviewsData.observeAsState(emptyList())
    LazyColumn {
        items(list) { data ->
            Text(text=data.toString())
        }
    }
}

@Composable
fun RestaurantWithImagesList(restaurantData: LiveData<List<RestaurantWithImages>>){
    val data by restaurantData.observeAsState(emptyList())
    if(data!=null){
        LazyColumn {
            items(data!!) { data ->
                Text(text=data.toString())
            }
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