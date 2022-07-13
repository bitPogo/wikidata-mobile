/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.util

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mediawiki.PublicApi
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import android.net.ConnectivityManager as AndroidConnection

@RunWith(RobolectricTestRunner::class)
class ConnectivityManagerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ConnectivityManager`() {
        ConnectivityManager(mockk()) fulfils PublicApi.ConnectivityManager::class
    }

    @Test
    fun `Given hasConnection is called, it returns false if activeNetworkInfo is null`() {
        // Given
        val context: Context = mockk()
        val connectivityManager: AndroidConnection = mockk()

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetworkInfo } returns null

        // When
        val actual = ConnectivityManager(context).hasConnection()

        // Then
        actual mustBe false
    }

    @Test
    fun `Given hasConnection is called, it returns the value activeNetworkInfo Connection`() {
        // Given
        val expected: Boolean = fixture.fixture()
        val context: Context = mockk()
        val connectivityManager: AndroidConnection = mockk()

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetworkInfo!!.isConnected } returns expected

        // When
        val actual = ConnectivityManager(context).hasConnection()

        // Then
        actual mustBe expected
    }
}
