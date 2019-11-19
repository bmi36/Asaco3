package com.example.asaco2

import android.app.Activity
import android.graphics.Bitmap
import android.os.AsyncTask
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PostBmpAsyncHttpRequest(activity: Activity) :
    AsyncTask<Param, Unit, String>() {

    private val mActivity by lazy {
        activity
    }

    override fun doInBackground(vararg params: Param?): String {

        val param = params[0]
        var connection: HttpURLConnection? = null
        val sb = StringBuilder()

        try {
            val jpg = ByteArrayOutputStream()
            param?.bmp?.compress(Bitmap.CompressFormat.JPEG,100,jpg)

            val url = URL(param?.uri)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.requestMethod = "POST"

            connection.setRequestProperty("User-Agent","Android")
            connection.setRequestProperty("Content-type","application/octet-stream")
            connection.doOutput = true
            connection.doInput = true
            connection.useCaches = false
            connection.connect()

            val out = BufferedOutputStream(connection.outputStream)
            out.write(jpg.toByteArray())
            out.flush()

            val ls = connection.inputStream
            val reader: BufferedReader = BufferedReader(InputStreamReader(ls,"UTF-8"))
            var line= reader.readLine()

            while ((line) != null){
                sb.append(line)
                ls.close()

                line = reader.readLine()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
        }
        return sb.toString()

    }

    override fun onPostExecute(result: String?) {
        TODO()
    }

}
