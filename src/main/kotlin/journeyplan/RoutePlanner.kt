package journeyplan

// representation of london underground map
class SubwayMap(val segments: List<Segment>) {
  fun routesFrom(
    origin: Station,
    destination: Station,
    optimisingFor: (Route) -> Int = { it.duration() }
  ): List<Route> {
    return filterSuspensions(
      findRoutes(origin, destination, listOf()).sortedBy(optimisingFor)
    )
  }

  // helper function to filter out suspended interchanges
  fun filterSuspensions(rs: List<Route>): List<Route> {
    val validRoutes = rs.filter { route ->
      route.segments.none { it.start.closed }
    }
    return validRoutes
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
      it.start == current && it.end !in visited && !it.line.suspended
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

// class that defines a specific route
class Route(val segments: List<Segment>) {
  // calculate change number
  fun numChanges(): Int {
    return segments
      .map { it.line }
      .filter { !(it.suspended) }
      .distinct()
      .size - 1
  }

  // calculate duration
  fun duration(): Int = segments
    .filter { !(it.line.suspended) }
    .sumOf { it.avgT }

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

// function for testing purposes
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
