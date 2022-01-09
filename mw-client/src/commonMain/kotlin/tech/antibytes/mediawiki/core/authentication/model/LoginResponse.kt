/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.authentication.model

import kotlinx.serialization.Serializable

@Serializable
internal class LoginResponse(
    val clientlogin: ClientLogin
)

@Serializable
internal class ClientLogin(
    val status: LoginStatus
)

@Serializable
internal enum class LoginStatus {
    PASS,
    FAIL,
    UI,
    REDIRECT,
    RESTART
}
