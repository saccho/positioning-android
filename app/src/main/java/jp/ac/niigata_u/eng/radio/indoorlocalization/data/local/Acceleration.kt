package jp.ac.niigata_u.eng.radio.indoorlocalization.data.local

class Acceleration(
  var x: Float = 0f,
  var y: Float = 0f,
  var z: Float = 0f
) {
  fun getX(): String = x.toString()
  fun getY(): String = y.toString()
  fun getZ(): String = z.toString()
}
