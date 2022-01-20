/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.wikibase.store.mock.client

import tech.antibytes.mediawiki.DataModelContract

data class PagePointerStub(
    override val revisionId: Long,
    override val title: String
) : DataModelContract.RevisionedPagePointer
