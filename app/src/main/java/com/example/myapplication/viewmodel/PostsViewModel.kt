package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Post
import com.example.myapplication.network.RetrofitClient
import kotlinx.coroutines.launch


//para generar los 4 estados de la accion en pantalla
sealed interface PostsUiState {
    data object Idle : PostsUiState
    data object Loading : PostsUiState
    data class Success(val posts: List<Post>) : PostsUiState
    data class Error(val message: String) : PostsUiState
}

class PostsViewModel: ViewModel() {

    var uiState: PostsUiState by mutableStateOf(PostsUiState.Idle)
    private set // solo el viewmodel puede cambiarlo, definido por los estados y solo a eso

    fun fetchPosts() {
        uiState = PostsUiState.Loading

        viewModelScope.launch {
            uiState = try {
                val posts = RetrofitClient.apiService.getPosts()
                PostsUiState.Success(posts)
            } catch (e: Exception) {
                PostsUiState.Error(e.localizedMessage ?:"Error")
            }

        }
    }

}


