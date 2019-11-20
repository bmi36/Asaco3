package com.example.asaco2

import android.graphics.Bitmap
import android.os.AsyncTask
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class PostBmpAsyncHttpRequest(
    private val resultMethod: Unit
) : AsyncTask<Param, Unit, String>() {



    override fun doInBackground(vararg params: Param?): String {

        val param = params[0]

        val connection: HttpURLConnection by lazy {
            URL(param?.uri).openConnection() as HttpURLConnection
        }

        val sb = StringBuilder()

        try {

            val jpg = ByteArrayOutputStream()
            param?.bmp?.compress(Bitmap.CompressFormat.JPEG, 100, jpg)

            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            connection.requestMethod = "POST"

            connection.setRequestProperty("User-Agent", "Android")
            connection.setRequestProperty("Content-type", "application/octet-stream")
            connection.doOutput = true
            connection.doInput = true
            connection.useCaches = false
            connection.connect()

            val out = BufferedOutputStream(connection.outputStream)
            out.write(jpg.toByteArray())
            out.flush()

            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line = reader.readLine()

            while ((line) != null) {
                sb.append(line)
                inputStream.close()

                line = reader.readLine()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }
        return sb.toString()

    }

    override fun onPostExecute(result: String?) {
        resultMethod
    }

}
