package journeyplan

// representation of london underground map
class SubwayMap(val segments: List<Segment>) {
  fun routesFrom(origin: Station, destination: Station): List<Route> {
    return findRoutes(origin, destination, listOf()).sortedBy { it.duration() }
  }

  fun findRoutes(
    current: Station,
    destination: Station,
    visited: List<Station>
  ): List<Route> {
    // base case - if destination reached, return a route with no segments
    if (current == destination) {
      return listOf(Route(emptyList()))
    }

    // prevent revisits
    val unvisitedSegments = segments.filter {
      it.start == current && it.end !in visited
    }

    // if no more valid segments then no routes
    if (unvisitedSegments.isEmpty()) {
      return emptyList()
    }

    // recursively get routes from each unvisited segment
    var routes = listOf<Route>()
    for (segment in unvisitedSegments) {
      // add current station to visited and start with end of segment
      val subRoutes = findRoutes(
        segment.end,
        destination,
        visited + current
      )
      // prepend current segment to sub-routes to build a route
      for (subRoute in subRoutes) {
        routes = routes + listOf(Route(listOf(segment) + subRoute.segments))
      }
    }

    return routes
  }
}

// creates and returns a subway map
fun londonUnderground(segments: List<Segment>): SubwayMap = SubwayMap(
  segments
)

fun displayAllRoutes(routes: List<Route>): String {
  // handle no route
  if (routes.isEmpty()) return "No travel required."

  // build string
  var output: String = ""

  for (route in routes) {
    output += (route.toString() + "\n")
  }

  return output
}

// class that defines a specific route
class Route(val segments: List<Segment>) {
  // calculate change number
  fun numChanges(): Int = segments.map { it.line }.distinct().size - 1

  // calculate duration
  fun duration(): Int = segments.sumOf { it.avgT }

  fun omitPath(): String {
    // omitted distinct callings
    val firstDistinct = segments.distinctBy { it.line.lineName }
    val lastDistinct = segments.reversed().distinctBy { it.line.lineName }
    val newLength = firstDistinct.size - 1

    // augment the distinct callings
    var routePath: String = ""
    for (i in 0..newLength) {
      routePath += " - ${firstDistinct[i].start.stationName} to " +
        "${lastDistinct[newLength - i].end.stationName} by " +
        "${firstDistinct[i].line}\n"
    }
    return (
      "${segments.first().start} to " +
        "${segments.last().end} - ${duration()} minutes, " +
        "${numChanges()} changes" +
        "\n$routePath"
      ).trimEnd()
  }

  override fun toString(): String {
    return when {
      segments.isEmpty() -> "No travel required."
      else -> omitPath()
    }
  }
}

// create a test map
val central = Line("Central")
val newL = Line("New")
val northActon = Station("North Acton")
val eastActon = Station("East Acton")
val whiteCity = Station("White City")
val shepherdsBush = Station("Shepherd's Bush")
val hollandPark = Station("Holland Park")
val nottingHillGate = Station("Notting Hill Gate")

val map = londonUnderground(
  listOf(
    Segment(northActon, nottingHillGate, newL, 11),
    Segment(northActon, eastActon, central, 2),
    Segment(eastActon, northActon, central, 2),
    Segment(eastActon, whiteCity, central, 3),
    Segment(whiteCity, eastActon, central, 3),
    Segment(whiteCity, shepherdsBush, central, 1),
    Segment(shepherdsBush, whiteCity, central, 1),
    Segment(shepherdsBush, hollandPark, central, 2),
    Segment(hollandPark, shepherdsBush, central, 2),
    Segment(hollandPark, nottingHillGate, central, 2),
    Segment(nottingHillGate, hollandPark, central, 2)
  )
)

val northernLine = Line("Northern")
val victoriaLine = Line("Victoria")
val centralLine = Line("Central")

val highgate = Station("Highgate")
val archway = Station("Archway")
val tufnellPark = Station("Tufnell Park")
val kentishTown = Station("Kentish Town")
val camden = Station("Camden Town")
val euston = Station("Euston")
val warrenStreet = Station("Warren Street")
val oxfordCircus = Station("Oxford Circus")
val bondStreet = Station("Bond Street")

val highgateToOxfordCircus =
  Route(
    listOf(
      Segment(highgate, archway, northernLine, 3),
      Segment(archway, kentishTown, northernLine, 3),
      Segment(kentishTown, camden, northernLine, 3),
      Segment(camden, euston, northernLine, 3),
      Segment(euston, warrenStreet, victoriaLine, 3),
      Segment(warrenStreet, oxfordCircus, victoriaLine, 3)
    )
  )

fun main() {
  val ss = map.routesFrom(northActon, nottingHillGate)
  println(displayAllRoutes(ss))
}
