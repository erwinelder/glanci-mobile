package com.ataglance.walletglance.categoryCollection.data.remote.source

import android.util.Log
import com.glanci.categoryCollection.shared.dto.CategoryCollectionWithAssociationsDto
import com.glanci.categoryCollection.shared.service.CategoryCollectionService
import kotlinx.rpc.krpc.ktor.client.KtorRpcClient
import kotlinx.rpc.withService

class CategoryCollectionRemoteDataSourceImpl(
    private val service: CategoryCollectionService
) : CategoryCollectionRemoteDataSource {

    constructor(client: KtorRpcClient) : this(
        service = client.withService<CategoryCollectionService>()
    )


    override suspend fun getUpdateTime(token: String): Long? {
        return runCatching {
            service.getUpdateTime(token = token)
        }.getOrNull().also { timestamp ->
            if (timestamp != null) {
                Log.d("CategoryCollectionRemoteDataSourceImpl", "getUpdateTime:" +
                        "received update time $timestamp")
            } else {
                Log.w("CategoryCollectionRemoteDataSourceImpl", "getUpdateTime:" +
                        "no update time received")
            }
        }
    }

    override suspend fun synchronizeCollectionsWithAssociations(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        token: String
    ): Boolean {
        return runCatching {
            service.saveCategoryCollectionsWithAssociations(
                collections = collections, timestamp = timestamp, token = token
            )
        }.isSuccess.also { success ->
            if (success) {
                Log.d("CategoryCollectionRemoteDataSourceImpl", "synchronizeCollectionsWithAssociations: " +
                        "synchronized ${collections.size} collections at timestamp $timestamp")
            } else {
                Log.e("CategoryCollectionRemoteDataSourceImpl", "synchronizeCollectionsWithAssociations: " +
                        "failed to synchronize ${collections.size} collections at timestamp $timestamp")
            }
        }
    }

    override suspend fun getCollectionsWithAssociationsAfterTimestamp(
        timestamp: Long,
        token: String
    ): List<CategoryCollectionWithAssociationsDto>? {
        return runCatching {
            service.getCategoryCollectionsWithAssociationsAfterTimestamp(
                timestamp = timestamp, token = token
            )
        }.getOrNull().also { collections ->
            collections?.forEach {
                Log.d("CategoryCollectionRemoteDataSourceImpl", "getCollectionsWithAssociationsAfterTimestamp:" +
                        "received collection: id = ${it.collection.id}")
            }
        }
    }

    override suspend fun synchronizeCollectionsWithAssociationsAndGetAfterTimestamp(
        collections: List<CategoryCollectionWithAssociationsDto>,
        timestamp: Long,
        localTimestamp: Long,
        token: String
    ): List<CategoryCollectionWithAssociationsDto>? {
        return runCatching {
            service.saveCategoryCollectionsWithAssociationsAndGetAfterTimestamp(
                collections = collections,
                timestamp = timestamp,
                localTimestamp = localTimestamp,
                token = token
            )
        }.getOrNull().also { collections ->
            Log.d("CategoryCollectionRemoteDataSourceImpl", "synchronizeCollectionsWithAssociationsAndGetAfterTimestamp:" +
                    "synchronized ${collections?.size ?: 0} collections at timestamp $timestamp" +
                    " with local timestamp $localTimestamp")
            collections?.forEach {
                Log.d("CategoryCollectionRemoteDataSourceImpl", "synchronizeCollectionsWithAssociationsAndGetAfterTimestamp:" +
                        "received collection: id = ${it.collection.id}")
            }
        }
    }

}