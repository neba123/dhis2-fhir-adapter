package org.dhis2.fhir.adapter.data.repository;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.dhis2.fhir.adapter.data.model.DataGroup;
import org.dhis2.fhir.adapter.data.model.StoredItem;
import org.dhis2.fhir.adapter.data.model.StoredItemId;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;

/**
 * Custom repository for {@linkplain StoredItem stored items}.
 *
 * @param <T> the concrete type of the stored item.
 * @param <I> the concrete type of the ID of the stored item.
 * @param <G> the group of the ID that is constant for a specific use case.
 * @author volsch
 */
public interface StoredItemRepository<T extends StoredItem<I, G>, I extends StoredItemId<G>, G extends DataGroup>
{
    boolean stored( @Nonnull G prefix, @Nonnull String storedId );

    boolean contains( @Nonnull G prefix, @Nonnull String storedId );

    @Nonnull
    Set<String> findProcessedIds( @Nonnull G prefix, @Nonnull Collection<String> processedIds );

    int deleteOldest( @Nonnull G prefix, @Nonnull Instant timestamp );
}
