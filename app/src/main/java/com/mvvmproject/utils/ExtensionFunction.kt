package com.mvvmproject.utils


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.mvvmproject.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import java.util.regex.Pattern
import kotlin.math.ln
import kotlin.math.pow

private const val TAG = "Utils"
var gson: Gson = GsonBuilder().create()
var toast: Toast? = null


fun showToast(context: Context, message: String) {
    toast?.cancel()
    toast = Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT)
    toast?.show()
}

fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

@SuppressLint("MissingPermission")
fun isNetworkAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> return true
            /*capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true*/
        }
    }
    return false
}

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.containsCapitalLetter(): Boolean {
    val expression = "(?=.*[A-Z]).{1,}"
    val pattern = Pattern.compile(expression, Pattern.UNICODE_CASE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.containsDigit(): Boolean {
    val expression = "^(?=.*\\d).{1,}$"
    val pattern = Pattern.compile(expression)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.containsSpecialChar(): Boolean {
    val expression = "^(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{1,}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.min8Characters(): Boolean {
    val expression = "^.{8,40}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun ImageView.loadImage(imageUrl: String?, placeholder: Int) {
    Glide.with(this.context)
        .load(imageUrl)
        .thumbnail(Glide.with(this.context).load(imageUrl))
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
        .placeholder(placeholder)
        .into(this)
}

fun getImageUploadPath(basePath: String): String {
    val dateTime = Calendar.getInstance().time
    val month = SimpleDateFormat("MMM", Locale.ENGLISH)
    val year = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val uuid = UUID.randomUUID().toString().lowercase(Locale.ROOT)
    return "$basePath${year.format(dateTime)}/${month.format(dateTime)}/${System.currentTimeMillis()}-$uuid-"
}


fun getImageName(basePath: String, type: String): String {
    return getImageUploadPath(basePath) + type + ".jpeg"
}

fun ImageView.toGrayScale() {
    val matrix = ColorMatrix().apply {
        setSaturation(0f)
    }
    colorFilter = ColorMatrixColorFilter(matrix)
}

fun getFormattedNumber(count: Long): String {
    return if (count <= 1000) {
        when (count) {
            in 1..50 -> "$count"
            in 50..100 -> "50+"
            in 100..500 -> "100+"
            in 500..1000 -> "500+"
            else -> "$count"
        }
    } else {
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        String.format("%d%c", count / 1000.0.pow(exp.toDouble()).toInt(), "KMGTPE"[exp - 1]) + "+"
    }
}

fun Button.disableButton() {
    isEnabled = false
    backgroundTintList =
        ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.white))
    //background = ContextCompat.getDrawable(this.context, R.drawable.btn_disabled_shape)
}

fun Button.enableButton() {
    isEnabled = true
    backgroundTintList =
        ColorStateList.valueOf(ContextCompat.getColor(this.context, R.color.white))
    //background = ContextCompat.getDrawable(this.context, R.drawable.btn_shape)
}

fun ProgressBar.show(list: ArrayList<*>?) {
    if (list.isNullOrEmpty() && visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun ProgressBar.remove() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    }
}

fun Double.toPriceFormat(): String {
    return DecimalFormat("0.00").format(this)
}

fun Float.toDecimalFormat(): String {
    return DecimalFormat("0.0").format(this)
}

fun convertKmToMiles(kilometers: Double): Double {
    return if (kilometers > 0) {
        kilometers * 0.621
    } else {
        0.0
    }
}

fun getHashKey(context: Context) {
    try {
        val info = context.packageManager.getPackageInfo(
            context.packageName, PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.e("MY_KEY_HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    } catch (_: PackageManager.NameNotFoundException) {
    } catch (_: NoSuchAlgorithmException) {
    }
}

fun shareApp(context: Context, title: String?, url: String?) {
    val share = Intent(Intent.ACTION_SEND)
    share.type = "text/plain"
    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
    share.putExtra(Intent.EXTRA_SUBJECT, title)
    share.putExtra(Intent.EXTRA_TEXT, url)
    context.startActivity(Intent.createChooser(share, "Share Link:"))
}

fun openPlayStoreLink(context: Context, url: String?) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}


@Throws(UnsupportedEncodingException::class)
fun openEmailClient(
    context: Context, email: String,
    subject: String?, body: String?,
) {
    var mSubject = subject
    var mBody = body
    if (mSubject == null) {
        mSubject = ""
    }
    if (mBody == null) {
        mBody = ""
    }
    val uriText = ("mailto:" + email + "?subject="
            + URLEncoder.encode(mSubject, "UTF-8") + "&body="
            + URLEncoder.encode(mBody, "UTF-8"))
    val uri = Uri.parse(uriText)
    val sendIntent = Intent(Intent.ACTION_SENDTO)
    sendIntent.data = uri
    context.startActivity(Intent.createChooser(sendIntent, "Send email"))
}


@SuppressLint("SetJavaScriptEnabled")
fun openWebView(
    webView: WebView,
    url: String?,
    progressBar: ProgressBar,
) {
    webView.settings.javaScriptEnabled = true
    webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
    webView.webViewClient = object : WebViewClient() {
        var urll =
            "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()"

        override fun onPageFinished(webview: WebView, url: String) {
            webView.loadUrl(urll)
            progressBar.visibility = View.GONE
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError,
        ) {
            super.onReceivedError(view, request, error)
        }
    }
    webView.loadUrl(url!!)
}

val String.lastPathComponent: String
    get() {
        var path = this
        if (path.endsWith("/"))
            path = path.substring(0, path.length - 1)
        var index = path.lastIndexOf('/')
        if (index < 0) {
            if (path.endsWith("\\"))
                path = path.substring(0, path.length - 1)
            index = path.lastIndexOf('\\')
            if (index < 0)
                return path
        }
        return path.substring(index + 1)
    }

inline fun <reified T> String.toObject(): T? {
    return gson.fromJson(this, T::class.java)
}

inline fun <reified T> String.toArrayList(): ArrayList<T> {
    return gson.fromJson(this, object : TypeToken<ArrayList<T>>() {}.type)
}

inline fun <reified T> String.toMutableList(): MutableList<T> {
    return if (this.isEmpty()) mutableListOf()
    else gson.fromJson(this, object : TypeToken<MutableList<T>>() {}.type)
}

fun View.show() {
    if (!isVisible) isVisible = true
}

fun View.remove() {
    if (!isGone) isGone = true
}

fun View.hide() {
    if (!isInvisible) isInvisible = true
}

fun String.toTitleCase(): String {
    val words = this.lowercase().split(" ")
    return buildString {
        for (word in words) {
            if (word.isNotEmpty()) {
                append(word.replaceFirstChar { it.uppercase() })
                append(" ")
            }
        }
    }.trim()
}

fun String.capitalizeWords(): String {
    val words = this.split(",").map { it.trim() } // Trim whitespace
    val capitalizedWords = words.map { word ->
        val regex = "\\b\\w".toRegex()
        word.replace(regex) { it.value.uppercase() }
    }
    return capitalizedWords.joinToString(", ")
}

fun String.formatAsTimeAgo(): String {
    val apiTime = this.parseDateString()
    val currentTime = Date()

    val timeDifferenceInMillis = currentTime.time - apiTime?.time!!

    val seconds = kotlin.math.abs(timeDifferenceInMillis) / 1000
    val minutes = seconds / 60

    if (minutes == 0L) {
        return "just now"
    } else if (minutes < 60) {
        return "$minutes minute${if (minutes != 1L) "s" else ""} ago"
    }

    val hours = minutes / 60
    if (hours < 24) {
        return "$hours hour${if (hours != 1L) "s" else ""} ago"
    }

    val days = hours / 24
    if (days < 30) {
        return "$days day${if (days != 1L) "s" else ""} ago"
    }

    val months = days / 30
    if (months < 12) {
        return "$months month${if (months != 1L) "s" else ""} ago"
    }

    val years = months / 12
    return "$years year${if (years != 1L) "s" else ""} ago"
}

fun String.parseDateString(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.parse(this)
}
