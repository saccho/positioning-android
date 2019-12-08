package jp.ac.niigata_u.eng.radio.indoorlocalization.data

data class ReceivedResult(
  var state: NetworkState,
  val data: MutableList<String>
)
