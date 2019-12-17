package jp.ac.niigata_u.eng.radio.indoorlocalization.ui.home

import android.content.Context
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import jp.ac.niigata_u.eng.radio.indoorlocalization.data.local.PreferencesDataSource

class HomeViewModel(context: Context) : ViewModel() {

  private val prefsDataSource: PreferencesDataSource =
    PreferencesDataSource(PreferenceManager.getDefaultSharedPreferences(context))

  fun saveIP(ip: String) = prefsDataSource.saveIP(ip)

  fun getIP(): String = prefsDataSource.getIP()

  fun savePort(port: Int) = prefsDataSource.savePort(port)

  fun getPort(): Int = prefsDataSource.getPort()

  @Suppress("UNCHECKED_CAST")
  class Factory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return HomeViewModel(context) as T
    }
  }
}
