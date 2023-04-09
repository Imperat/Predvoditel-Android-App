package com.example.predvoditel

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var jwtToken: String? = null;
    var refreshToken: String? = null;
    var userId: String? = null;
}