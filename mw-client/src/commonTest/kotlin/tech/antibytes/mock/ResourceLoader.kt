/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.mock

import tech.anitbytes.mediawiki.test.config.test.config.TestConfig
import tech.antibytes.util.test.CommonResourceLoader
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ResourceLoader {
    val loader = CommonResourceLoader(TestConfig.projectDir)
}
