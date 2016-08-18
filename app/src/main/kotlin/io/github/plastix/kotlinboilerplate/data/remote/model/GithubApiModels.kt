package io.github.plastix.kotlinboilerplate.data.remote.model

import com.google.gson.annotations.SerializedName
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable

data class SearchResponse(

        @SerializedName("total_count")
        val count: Int,

        @SerializedName("items")
        val repos: List<Repo>
)

@PaperParcel
data class Repo(

        val name: String,

        @SerializedName("full_name")
        val fullName: String,

        val owner: Owner,

        val description: String,

        @SerializedName("stargazers_count")
        val stars: Int,

        val forks: Int
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelable.Creator(Repo::class.java)
    }
}

@PaperParcel
data class Owner(
        @SerializedName("login")
        val name: String,

        @SerializedName("avatar_url")
        val avatarUrl: String
) : PaperParcelable {
    companion object {
        @JvmField val CREATOR = PaperParcelable.Creator(Owner::class.java)
    }
}