package sa.edu.tuwaiq.project_01.model

import java.util.*

data class Post(
    var postContent: String = "",
    var postImage: String = "",
    var username: String = "",
    var userImage: String = "",
    var userId: String = "",
    var postID:String=UUID.randomUUID().toString()
        )
