package com.example.databasedependencies

import com.example.databasedependencies.db.Database

interface DatabaseDependencies {
    fun provideDataBase() : Database
}