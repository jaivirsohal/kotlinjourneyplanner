package journeyplan

class Station(val stationName: String) {
  override fun toString(): String {
    return stationName
  }
}

class Line(val lineName: String) {
  override fun toString(): String {
    return "$lineName Line"
  }
}

class Segment(val start: Station, val end: Station, val line: Line, val avgT: Int) {
  override fun toString(): String {
    return "${start.stationName} " +
      "-> ${end.stationName} " +
      "on ${line.lineName} Line " +
      "($avgT min)"
  }
}
