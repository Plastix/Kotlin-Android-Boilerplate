package io.github.plastix.kotlinboilerplate.data.remote.model

import android.os.Parcel
import android.os.Parcelable

// Reduce boilerplate in creating parcelables
// From http://stackoverflow.com/questions/33551972/is-there-a-convenient-way-to-create-parcelable-data-classes-in-android-with-kotl/35700144#35700144
// Opting for this since annotation processing libraries (like PaperParcel) can have issues on Kotlin
// when using Dagger 2
// See https://github.com/gen0083/KotlinDaggerDataBinding
interface DefaultParcelable : Parcelable {
    override fun describeContents(): Int = 0

    companion object {
        fun <T> generateCreator(create: (source: Parcel) -> T): Parcelable.Creator<T> = object: Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T = create(source)

            override fun newArray(size: Int): Array<out T>? = newArray(size)
        }

    }
}
inline fun <reified T> Parcel.read(): T = readValue(T::class.javaClass.classLoader) as T
fun Parcel.write(vararg values: Any?) = values.forEach { writeValue(it) }

