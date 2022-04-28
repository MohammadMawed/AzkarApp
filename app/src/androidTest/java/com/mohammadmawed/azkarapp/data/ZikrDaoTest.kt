package com.mohammadmawed.azkarapp.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ZikrDaoTest {
    private lateinit var database: ZikrDatabase
    private lateinit var dao: ZikrDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ZikrDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.zikrDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun addZikr() = runBlocking {

        val zikr = Zikr(1, "name", "hint", 3, alsabah = true, wasRead = false)
        dao.addZikr(zikr)

        val expected = Zikr(1, "name", "hint", 3, alsabah = true, wasRead = false)

        assertEquals(expected, zikr)
    }

    @Test
    fun getAlsabahZikr() = runBlocking {

        val zikr = Zikr(1, "name", "hint", 3, alsabah = false, wasRead = false)
        dao.addZikr(zikr)

        val zikrFetched = dao.getAlsabahZikr(1, false).first().get(0)

        assertEquals(zikr, zikrFetched)
    }

    @Test
    fun getAlmasahZikr() = runBlocking {

        val zikr = Zikr(1, "name", "hint", 3, alsabah = true, wasRead = false)
        dao.addZikr(zikr)

        val zikrFetched = dao.getAlmasahZikr(1, true).first().get(0)

        assertEquals(zikr, zikrFetched)
    }

    @Test
    fun getZikr() = runBlocking {

        val zikr = Zikr(1, "name", "hint", 3, alsabah = true, wasRead = false)
        dao.addZikr(zikr)

        val zikrFetched = dao.getZikr().first().get(0)

        assertEquals(zikr, zikrFetched)
    }
}