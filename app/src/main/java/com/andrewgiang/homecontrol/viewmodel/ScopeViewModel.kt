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

package com.andrewgiang.homecontrol.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andrewgiang.homecontrol.DispatchProvider
import com.andrewgiang.homecontrol.ui.Nav
import com.andrewgiang.homecontrol.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

open class ScopeViewModel constructor(dispatcherProvider: DispatchProvider) : ViewModel(), CoroutineScope {
    override val coroutineContext = Job() + dispatcherProvider.main

    override fun onCleared() {
        coroutineContext.cancel()
    }

    protected val navigationState = SingleLiveEvent<Nav>()

    fun getNavState(): LiveData<Nav> {
        return navigationState
    }
}