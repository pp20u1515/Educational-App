package com.example.programmingc.presentation.additional_functions

fun isValidInput(email: String): Boolean{
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(email)
}

fun checkValidEmail(email: String): Boolean{
    var rc: Boolean = false // return_code

    if (email.isNotBlank() && isValidInput(email)){
        rc = true
    }
    return rc
}

fun checkValidPassword(password: String): Boolean{
    var rc: Boolean = false

    if (password.isNotBlank()){
        val firstChar = password[0]

        if (password.length >= 6){
            rc = true
        }
    }
    return rc
}

fun checkInput(email: String, password: String): Boolean{
    var rc: Boolean = false

    if (checkValidEmail(email)){
        if (checkValidPassword(password)){
            rc = true
        }
    }
    return rc
}