package jp.ac.niigata_u.eng.radio.indoorlocalization.data.local

import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesDataSource(private val prefs: SharedPreferences) : PreferencesService {

  override fun saveIP(ip: String) = prefs.edit(true) {
    putString(KEY_IP, ip)
  }

  override fun getIP(): String = prefs.getString(KEY_IP, "") ?: ""

  override fun savePort(port: Int) = prefs.edit(true) {
    putInt(KEY_PORT, port)
  }

  override fun getPort(): Int = prefs.getInt(KEY_PORT, -1)

  companion object {
    const val KEY_IP = "key_ip"
    const val KEY_PORT = "key_port"
  }
}
