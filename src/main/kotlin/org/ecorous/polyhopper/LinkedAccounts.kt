package org.ecorous.polyhopper


data class PlayerDBResponse (val code: String, val message: String, val data: PlayerDBData)
data class PlayerDBData (val player: PlayerDBPlayer)
data class PlayerDBPlayer (val username: String, val id: String, val raw_id: String, val avatar: String)
data class LinkedAccounts (val accounts: List<Account>)
data class Account (val discordID: String, val minecraftUUIDS: List<String>)
