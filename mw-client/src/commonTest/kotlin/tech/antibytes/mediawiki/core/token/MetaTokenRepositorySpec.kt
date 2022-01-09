/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mediawiki.core.token

import tech.antibytes.mock.token.MetaTokenApiServiceStub
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class MetaTokenRepositorySpec {
    @Test
    fun `It fulfils Repository`() {
        MetaTokenRepository(MetaTokenApiServiceStub()) fulfils MetaTokenServiceContract.Repository::class
    }

    @Test
    fun `Given fetchToken is called with a TokenType it returns a MetaToken`() {

    }
}
