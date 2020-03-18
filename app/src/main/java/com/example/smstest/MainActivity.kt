package com.example.smstest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var webView = findViewById<WebView>(R.id.web_view)
        var webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportMultipleWindows(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        

        var smsCertJson:JSONObject = getSmsCert()
        var smsCert = smsCertJson.getString("sms_cert")
        var postData = "tr_cert="+smsCert+"&tr_url=http://15.164.212.180:8080/sms/registPhone&tr_add=N"
//        webView.loadUrl("https://www.kmcert.com/kmcis/web/kmcisReq.jsp")

        webView.postUrl("https://www.kmcert.com/kmcis/web/kmcisReq.jsp", postData.toByteArray())
//        thread {
//            var isShutDown = true
//            while (isShutDown) {
//                runOnUiThread(Runnable {
//                    if(webView.url.equals("http://15.164.212.180:8080/sms/servicePage2")){
//                        webView.visibility = View.GONE
//                        isShutDown = false
//                    }
//                })
//            }
//
//
//        }.start()


        // tr_cert=trc&tr_url=url&tr_add=N


    }

    public fun getSmsCert(): JSONObject {
        var smsCert:JSONObject? =null
        val thread:Thread = Thread(Runnable {
            kotlin.run {
                try{


                    var body = JSONObject();
                    body.put("aa", "aa")
                    val homeDataProtocolURL: URL = URL("http://15.164.212.180:8080/sms/smsCert")
                    val homeDataProtocolConnection: HttpURLConnection = homeDataProtocolURL.openConnection() as HttpURLConnection
                    homeDataProtocolConnection.requestMethod = "POST"
                    homeDataProtocolConnection.setRequestProperty("Accept-Charset", "UTF-8")
                    homeDataProtocolConnection.setRequestProperty("Content-Type", "application/json");
                    homeDataProtocolConnection.connectTimeout = 3000
                    homeDataProtocolConnection.readTimeout = 3000
                    homeDataProtocolConnection.connect()

                    val bufferedWriter: BufferedWriter =homeDataProtocolConnection.outputStream.bufferedWriter()
                    val printWriter: PrintWriter = PrintWriter(bufferedWriter)
                    printWriter.println(body.toString())
                    printWriter.flush()
                    printWriter.close()

                    BufferedReader(InputStreamReader(homeDataProtocolConnection.inputStream)).use {
                        var inputLine = it.readLine()
                        smsCert = JSONObject(inputLine)
                        it.close()
                    }
                }catch (e:Throwable) {
                    Log.e("cert___error", e.toString())
                }
            }
        })

        thread.start()
        thread.join()
        return smsCert!!
    }

    public fun signin(email:String, passwd:String):JSONObject {
        var setplayMatch:JSONObject? =null
        val thread:Thread = Thread(Runnable {
            kotlin.run {
                try{


                    val body:JSONObject = JSONObject()
                    body.put("id", email)
                    body.put("password", passwd)
                    val homeDataProtocolURL: URL = URL("http://localhost:8080/signin")
                    val homeDataProtocolConnection: HttpURLConnection = homeDataProtocolURL.openConnection() as HttpURLConnection
                    homeDataProtocolConnection.requestMethod = "POST"
                    homeDataProtocolConnection.setRequestProperty("Accept-Charset", "UTF-8")
                    homeDataProtocolConnection.setRequestProperty("Content-Type", "application/json");
                    homeDataProtocolConnection.connectTimeout = 3000
                    homeDataProtocolConnection.readTimeout = 3000
                    homeDataProtocolConnection.connect()

                    val bufferedWriter: BufferedWriter =homeDataProtocolConnection.outputStream.bufferedWriter()
                    val printWriter: PrintWriter = PrintWriter(bufferedWriter)
                    printWriter.println(body.toString())
                    printWriter.flush()
                    printWriter.close()


                    // 서버의 응답값을 보조스트림으로 감싸서 String 형태로 가져옴

                    BufferedReader(InputStreamReader(homeDataProtocolConnection.inputStream)).use {
                        val response = StringBuffer()
                        var inputLine = it.readLine()
                        Log.i("signin!!", inputLine.toString())
                        setplayMatch = JSONObject(inputLine)
                        it.close()
                    }
                }catch (e:Throwable) {
                    Log.e("throw___", e.toString())
                }
            }
        })

        thread.start()
        thread.join()
        return setplayMatch!!
    }
}
