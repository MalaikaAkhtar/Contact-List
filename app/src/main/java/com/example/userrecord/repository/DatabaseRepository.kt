package com.example.userrecord.repository

import com.example.userrecord.db.ContactsDao
import com.example.userrecord.db.ContactsEntity
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val dao: ContactsDao) {
    suspend fun saveContact(entity: ContactsEntity) = dao.saveContact(entity)
    fun getAllContacts() = dao.getAllContacts()
    fun deleteAllContacts() = dao.deleteAllContacts()
    fun sortedASC() = dao.sortedASC()
    fun sortedDESC() = dao.sortedDESC()
    fun searchContact(name: String) = dao.searchContact(name)
}