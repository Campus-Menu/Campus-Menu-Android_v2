package com.arikan.campusmenu.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

data class StudentAccount(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val studentNumber: String,
    val department: String
)

object StudentRepository {
    private const val FILE_NAME = "students.json"
    private val students = mutableStateListOf<StudentAccount>()
    private val gson = Gson()
    
    fun initialize(context: Context) {
        loadStudents(context)
    }
    
    private fun loadStudents(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (file.exists()) {
                val json = file.readText()
                val type = object : TypeToken<List<StudentAccount>>() {}.type
                val loadedStudents: List<StudentAccount> = gson.fromJson(json, type)
                students.clear()
                students.addAll(loadedStudents)
            } else {
                // Demo öğrenci ekle
                students.add(
                    StudentAccount(
                        id = "1",
                        name = "Demo Öğrenci",
                        email = "ogrenci@campus.com",
                        password = "123456",
                        studentNumber = "2021001",
                        department = "Bilgisayar Mühendisliği"
                    )
                )
                saveStudents(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun saveStudents(context: Context) {
        try {
            val file = File(context.filesDir, FILE_NAME)
            val json = gson.toJson(students)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun register(
        context: Context,
        name: String,
        email: String,
        password: String,
        studentNumber: String,
        department: String
    ): Result<StudentAccount> {
        // Email kontrolü
        if (students.any { it.email.equals(email, ignoreCase = true) }) {
            return Result.failure(Exception("Bu e-posta adresi zaten kayıtlı"))
        }
        
        // Öğrenci numarası kontrolü
        if (students.any { it.studentNumber == studentNumber }) {
            return Result.failure(Exception("Bu öğrenci numarası zaten kayıtlı"))
        }
        
        val newStudent = StudentAccount(
            id = (students.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0 + 1).toString(),
            name = name,
            email = email,
            password = password,
            studentNumber = studentNumber,
            department = department
        )
        
        students.add(newStudent)
        saveStudents(context)
        
        return Result.success(newStudent)
    }
    
    fun login(email: String, password: String): StudentAccount? {
        return students.find { 
            it.email.equals(email, ignoreCase = true) && it.password == password 
        }
    }
    
    fun getAllStudents(): List<StudentAccount> = students.toList()
}
