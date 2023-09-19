package me.proxui.dataholders.database

interface IDatabase {
    /**
     * @returns the collection with [name] and creates a new one if non-existent
     */
    fun getCollection(name: String) : IDataCollection

    /**
     * Closes the connection to the database server
     */
    fun close()
}