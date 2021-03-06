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

package com.andrewgiang.homecontrol.workmanager

import android.app.Application
import androidx.work.Constraints
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkRequestFactory @Inject constructor(app: Application) {
    private val authWorkerId = app.applicationContext.packageName.plus("auth_refresh_id")
    private val entityId = app.applicationContext.packageName.plus("entity_state_refresh_id")

    val workRequests = listOf(
        authWorkerId to createWorkRequest<AuthTokenWorker>(
            repeatInterval = 30,
            timeUnit = TimeUnit.MINUTES,
            constraints = defaultConstraints()
        ),
        entityId to createWorkRequest<EntitySyncWorker>(
            repeatInterval = 6,
            timeUnit = TimeUnit.HOURS,
            constraints = defaultConstraints()
        )
    )

    fun defaultConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    private inline fun <reified W : ListenableWorker> createWorkRequest(
        repeatInterval: Long,
        timeUnit: TimeUnit,
        constraints: Constraints
    ): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<W>(repeatInterval, timeUnit)
            .setConstraints(constraints)
            .build()
    }
}
