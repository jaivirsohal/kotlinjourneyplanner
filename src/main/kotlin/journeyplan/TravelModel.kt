package journeyplan

class Station(val stationName: String, var closed: Boolean = false) {
  fun close() {
    this.closed = true
  }

  fun open() {
    this.closed = false
  }

  override fun toString(): String {
    return stationName
  }
}

class Line(val lineName: String, var suspended: Boolean = false) {
  fun suspend() {
    this.suspended = true
  }

  fun resume() {
    this.suspended = false
  }

  override fun toString(): String {
    return "$lineName Line"
  }
}

class Segment(
  val start: Station,
  val end: Station,
  val line: Line,
  val avgT: Int
) {
  override fun toString(): String {
    return "${start.stationName} " +
      "-> ${end.stationName} " +
      "on ${line.lineName} Line " +
      "($avgT min)"
  }
}
