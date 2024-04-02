package org.nekoweb.amycatgirl.revolt.models.api.channels

import org.nekoweb.amycatgirl.revolt.models.api.Attachment

sealed class Channel(
    val channelType: String,
    val id: String
) {
    class SavedMessages(
        id: String,
        val userId: String
    ) : Channel("SavedMessages", id)

    class DirectMessage(
        id: String,
        val active: Boolean,
        val recipients: List<String>,
        val lastSentMessageId: String? = null
    ) : Channel("DirectMessage", id)

    class Group(
        id: String,
        val name: String,
        val ownerId: String,
        val description: String? = null,
        val recipients: List<String>,
        val icon: Attachment,
        val lastSentMessage: String,
        val memberPermissions: Int,
        val notSafeForWork: Boolean
    ) : Channel("Group", id)
}