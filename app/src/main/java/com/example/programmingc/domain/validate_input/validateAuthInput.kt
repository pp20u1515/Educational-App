package com.example.programmingc.domain.validate_input

import com.example.programmingc.domain.model.ValidAuthInput

fun isValidEmail(email: String): Boolean{
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    return emailRegex.matches(email)
}

fun checkValidEmail(email: String): ValidAuthInput{
    var rc: ValidAuthInput

    if (email.isNotBlank()){
        if (isValidEmail(email)){
            rc = ValidAuthInput.OK
        }
        else{
            rc = ValidAuthInput.NOT_VALID_EMAIL
        }
    }
    else{
        rc = ValidAuthInput.EMPTY_EMAIL
    }
    return rc
}

fun checkValidPassword(password: String): ValidAuthInput{
    var rc: ValidAuthInput

    if (password.isNotBlank()){
        if (password.length >= 6){
            rc = ValidAuthInput.OK
        }
        else{
            rc = ValidAuthInput.SHORT_PASSWORD
        }
    }
    else{
        rc = ValidAuthInput.EMPTY_PASSWORD
    }
    return rc
}

fun checkInput(email: String, password: String): ValidAuthInput{
    var rc = checkValidEmail(email)

    if (rc == ValidAuthInput.OK){
        rc = checkValidPassword(password)
    }
    return rc
}