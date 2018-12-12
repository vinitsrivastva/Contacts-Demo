package com.contactlist.model

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import io.reactivex.BackpressureStrategy
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


data class SimpleContact(val displayName: String, val phoneNumber: String) {
    companion object {
        fun from(cursor: Cursor) =
                SimpleContact(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
    }
}


fun getContacts(contentResolver: ContentResolver): Flowable<List<SimpleContact>> =
        contentResolver.uriChangesOf(
                {
                    itemsFor(
                            { query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    null, null,
                                    "${Contacts.DISPLAY_NAME} ASC") },
                            { SimpleContact.from(it) }
                    )
                }, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .concatMap { obs -> obs.toList().toFlowable() }

inline fun <T> ContentResolver.uriChangesOf(
        crossinline onUriChanged: ContentResolver.() -> T,
        vararg uris: Uri, notifyForDescendants: Boolean = false): Flowable<T> =
        Flowable.create<T>({ emitter ->
            val contentObserver = object : ContentObserver(Handler()) {
                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    emitter.onNext(onUriChanged())
                }
            }
            uris.forEach {
                registerContentObserver(it, notifyForDescendants, contentObserver)
            }
            emitter.onNext(onUriChanged())
            emitter.setCancellable { unregisterContentObserver(contentObserver) }
        }, BackpressureStrategy.LATEST)


inline fun <T : Any> ContentResolver.itemsFor(
        crossinline queryRawData: ContentResolver.() -> Cursor,
        crossinline mapRawData: (Cursor) -> T): Flowable<T> =
        Flowable.generate<T, Cursor>(
                Callable<Cursor> { queryRawData(this) },
                BiFunction<Cursor, Emitter<T>, Cursor> { cursor, emitter ->
                    if (cursor.moveToNext()) {
                        emitter.onNext(mapRawData(cursor))
                    } else {
                        emitter.onComplete()
                    }
                    return@BiFunction cursor
                },
                Consumer<Cursor> { cursor -> cursor.close() }
        )