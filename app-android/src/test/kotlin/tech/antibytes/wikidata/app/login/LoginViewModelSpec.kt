/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikidata.app.login

import androidx.lifecycle.ViewModel
import org.junit.Test
import tech.antibytes.util.test.fulfils

class LoginViewModelSpec {
    @Test
    fun `It fulfils LoginViewModel`() {
        val viewmodel = LoginViewModel()

        viewmodel fulfils LoginContract.LoginViewModel::class
        viewmodel fulfils ViewModel::class
    }

    @Test
    fun `
}
