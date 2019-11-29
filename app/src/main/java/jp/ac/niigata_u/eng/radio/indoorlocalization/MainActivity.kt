package jp.ac.niigata_u.eng.radio.indoorlocalization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import jp.ac.niigata_u.eng.radio.indoorlocalization.ui.MainFragment

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, MainFragment.newInstance())
        .commitNow()
    }
  }

}
