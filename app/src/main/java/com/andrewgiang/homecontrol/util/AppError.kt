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

package com.andrewgiang.homecontrol.util

import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AppError(val msg: String) {
    data class NetworkError(val networkMsg: String = "Unknown") :
        AppError(String.format("Network Error: %s", networkMsg))

    object UnknownError : AppError("Unknown Error")

    companion object {
        fun from(e: Throwable): AppError {
            return when (e) {
                is SocketTimeoutException -> networkError(e)
                is UnknownHostException -> networkError(e)
                else -> UnknownError
            }
        }

        private fun networkError(e: Throwable): AppError {
            return e.message?.let { message ->
                NetworkError(message)
            } ?: NetworkError()
        }
    }
}