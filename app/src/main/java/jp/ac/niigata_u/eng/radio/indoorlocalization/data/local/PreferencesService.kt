package jp.ac.niigata_u.eng.radio.indoorlocalization.data.local

interface PreferencesService {
  fun saveIP(ip: String)

  fun getIP(): String

  fun savePort(port: Int)

  fun getPort(): Int
}
