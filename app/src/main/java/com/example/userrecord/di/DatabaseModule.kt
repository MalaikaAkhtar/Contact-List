package com.example.userrecord.di

import android.content.Context
import androidx.room.Room
import com.example.userrecord.db.ContactsDB
import com.example.userrecord.db.ContactsEntity
import com.example.userrecord.utils.Constants.CONTACTS_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, ContactsDB::class.java,CONTACTS_DATABASE
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: ContactsDB) = db.contactDao()

    @Provides
    @Singleton
    fun provideEntity() = ContactsEntity()
}