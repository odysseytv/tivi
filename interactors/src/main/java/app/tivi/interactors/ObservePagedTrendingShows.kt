/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.tivi.interactors

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import app.tivi.data.repositories.trendingshows.TrendingShowsRepository
import app.tivi.data.resultentities.TrendingEntryWithShow
import app.tivi.util.AppRxSchedulers
import io.reactivex.Observable
import javax.inject.Inject

class ObservePagedTrendingShows @Inject constructor(
    private val schedulers: AppRxSchedulers,
    private val trendingShowsRepository: TrendingShowsRepository
) : PagingInteractor<ObservePagedTrendingShows.Params, TrendingEntryWithShow>() {
    override fun createObservable(params: Params): Observable<PagedList<TrendingEntryWithShow>> {
        val source = trendingShowsRepository.observeForPaging()
        return RxPagedListBuilder(source, params.pagingConfig)
                .setBoundaryCallback(params.boundaryCallback)
                .setFetchScheduler(schedulers.io)
                .setNotifyScheduler(schedulers.main)
                .buildObservable()
    }

    data class Params(
        override val pagingConfig: PagedList.Config,
        override val boundaryCallback: PagedList.BoundaryCallback<TrendingEntryWithShow>?
    ) : Parameters<TrendingEntryWithShow>
}
