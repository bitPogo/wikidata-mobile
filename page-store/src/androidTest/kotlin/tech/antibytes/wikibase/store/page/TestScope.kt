/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.page

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

actual val testScope1: CoroutineScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
actual val testScope2: CoroutineScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
