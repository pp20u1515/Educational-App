package com.example.programmingc.utils

import java.lang.reflect.Proxy
/*
class Retrofit {
    class Builder{
        fun baseUrl(url: String): Builder{
            return this
        }
        fun build(): Retrofit{
            return Retrofit()
        }
    }
    inline fun <reified T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(
            clazz.classLoader,
            arrayOf(clazz),
            RetrofitInvocactionHandler()
        ) as T
    }

}*/