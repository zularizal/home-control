/*
 * Copyright 2018 Andrew Giang
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.andrewgiang.homecontrol.api

import com.andrewgiang.assistantsdk.Api
import com.andrewgiang.assistantsdk.ApiFactory
import com.andrewgiang.homecontrol.CLIENT_ID
import com.andrewgiang.homecontrol.data.AuthPrefs
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit

class ApiHolderTest {

    val mockPrefs: AuthPrefs = mockk()
    val mockBuilder: Retrofit.Builder = mockk()
    val mockApiFactory: ApiFactory = mockk()
    val subject: ApiHolder = ApiHolder(mockBuilder, mockPrefs, mockApiFactory)

    @Test(expected = IllegalStateException::class)
    fun expect_illegal_argument_exception_if_mock_prefs_does_not_have_host_url_set() {
        every { mockPrefs.getHostUrl() } returns null
        subject.api
    }

    @Test
    fun expect_api_creation_with_host_and_builder() {
        val mockApi = mockValidApi()

        assertEquals(mockApi, subject.api)
    }

    /**
     * This is required due to avoiding a circular dependency with the dagger graph
     */
    @Test
    fun test_sets_the_AuthTokenAuthenticator_api_when_accesing_api_from_holder() {
        val mockApi = mockValidApi()

        assertEquals(mockApi, subject.api)

        assertEquals(mockApi, AuthTokenAuthenticator.api)
    }

    private fun mockValidApi(): Api {
        val expectedHost = "https://www.google.com"
        every { mockPrefs.getHostUrl() } returns expectedHost
        val mockApi = mockk<Api>()

        every {
            mockApiFactory.create(
                eq(mockBuilder),
                eq(expectedHost),
                eq(CLIENT_ID)
            )
        } returns mockApi
        return mockApi
    }
}