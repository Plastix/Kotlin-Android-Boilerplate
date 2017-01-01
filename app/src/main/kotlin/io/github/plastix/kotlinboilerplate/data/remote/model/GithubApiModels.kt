package io.github.plastix.kotlinboilerplate.data.remote.model

import android.os.Parcel
import com.google.gson.annotations.SerializedName

data class SearchResponse(

        @SerializedName("total_count")
        val count: Int,

        @SerializedName("items")
        val repos: List<Repo>
)

data class Repo(

        val name: String,

        @SerializedName("full_name")
        val fullName: String,

        val owner: Owner,

        val description: String,

        @SerializedName("stargazers_count")
        val stars: Int,

        val forks: Int
) : DefaultParcelable {
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.write(name, fullName, owner, description, stars, forks)
    }

    companion object {
        @JvmField val CREATOR = DefaultParcelable.generateCreator {
            Repo(it.read(), it.read(), it.read(), it.read(), it.read(), it.read())
        }
    }
}

data class Owner(
        @SerializedName("login")
        val name: String,

        @SerializedName("avatar_url")
        val avatarUrl: String
) : DefaultParcelable {

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.write(name, avatarUrl)
    }

    companion object {
        @JvmField val CREATOR = DefaultParcelable.generateCreator {
            Owner(it.read(), it.read())
        }
    }
}