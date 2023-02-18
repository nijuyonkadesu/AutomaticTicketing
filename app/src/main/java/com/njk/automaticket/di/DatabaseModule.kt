package com.njk.automaticket.di

import android.content.Context
import androidx.room.Room
import com.njk.automaticket.data.ProfileDao
import com.njk.automaticket.data.ProfileDatabase
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
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): ProfileDatabase = Room.databaseBuilder(
        context.applicationContext,
        ProfileDatabase::class.java,
        "profile-database"
        ).fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideProfileDao(
        profileDatabase: ProfileDatabase,
    ): ProfileDao = profileDatabase.profileDao()
}