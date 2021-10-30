package com.example.s17149.ContentProvider

import com.example.s17149.DataBase.MyRoomDatabase.Companion.getDatabase
import android.content.ContentProvider
import com.example.s17149.ContentProvider.ContentProviderTest
import android.content.UriMatcher
import com.example.s17149.DataBase.MyRoomDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.s17149.DataBase.Product
import com.example.s17149.DataBase.ProductDao
import com.example.s17149.Logic.AppLogic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.HashMap

class ContentProviderTest : ContentProvider() {
    companion object {
        // defining authority so that other application can access it
        const val PROVIDER_NAME = "com.example.s17149.DataBase.MyRoomDatabase"

        // defining content URI
        const val URL = "content://" + PROVIDER_NAME + "/product"

        // parsing the content URI
        val CONTENT_URI = Uri.parse(URL)
        const val id = "id"
        const val name = "name"
        const val qty = "qty"
        const val price = "price"
        const val click = "click"
        const val uriCode = 1
        val uriMatcher: UriMatcher?
        private val values: HashMap<String, String>? = null

        // declaring name of the database
        const val DATABASE_NAME = "ProductDatabase"

        // declaring table name of the database
        const val TABLE_NAME = "product"

        // declaring version of the database
        const val DATABASE_VERSION = 1

        // sql query to create the table
        const val CREATE_DB_TABLE = (" CREATE TABLE " + TABLE_NAME
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " name TEXT NOT NULL);")

        init {

            // to match the content URI
            // every time user access table under content provider
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            // to access whole table
            uriMatcher.addURI(PROVIDER_NAME, "product", uriCode)

            // to access a particular row
            // of the table
            uriMatcher.addURI(PROVIDER_NAME, "product/*", uriCode)
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher!!.match(uri)) {
            uriCode -> "vnd.android.cursor.dir/product"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    // creating the database
    override fun onCreate(): Boolean {
        val context = context
        db = getDatabase(context!!.applicationContext).productDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val arr = arrayOf<String>(id, name, qty, price, click);
        val cursor = MatrixCursor(arr);
        val products = db.getProducts().value!!;

        for(product: Product in products){
            val values = arrayOf(product.id,product.name,product.qty,product.price,product.click)
            cursor.addRow(values)
        }

        return cursor;
    }

    // adding data to the database
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val product = Product(
            values!!.getAsLong(id),
            values.getAsString(name),
            values.getAsFloat(qty),
            values.getAsFloat(price),
            values.getAsBoolean(click)
        )
        CoroutineScope(Dispatchers.IO).launch { db.insert(AppLogic.product) }
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,selectionArgs: Array<String>?
    ): Int {
        val product = Product(
            values!!.getAsLong(id),
            values.getAsString(name),
            values.getAsFloat(qty),
            values.getAsFloat(price),
            values.getAsBoolean(click)
        )
        CoroutineScope(Dispatchers.IO).launch { db.update(AppLogic.product) }
        return -1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var count = 0
        val product = Product(selection!!.get(0).toLong(),"",0f,0f,false);
        CoroutineScope(Dispatchers.IO).launch { db.delete(AppLogic.product) }
        return -1
    }
    // creating object of database
    // to perform query
    private lateinit var db: ProductDao

}