package journeyplan

import org.junit.Assert.assertEquals
import org.junit.Test

class RoutePlannerTest {

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

  val tufnellParkToHighgate =
    Route(
      listOf(
        Segment(tufnellPark, archway, northernLine, 3),
        Segment(archway, highgate, northernLine, 3)
      )
    )

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

  val camdenToBondStreet =
    Route(
      listOf(
        Segment(camden, euston, northernLine, 3),
        Segment(euston, warrenStreet, victoriaLine, 3),
        Segment(warrenStreet, oxfordCircus, victoriaLine, 3),
        Segment(oxfordCircus, bondStreet, centralLine, 2)
      )
    )

  // create a test map
  val central = Line("Central")
  val newL = Line("New")
  val northActon = Station("North Acton")
  val eastActon = Station("East Acton")
  val whiteCity = Station("White City")
  val shepherdsBush = Station("Shepherd's Bush")
  val hollandPark = Station("Holland Park")
  val nottingHillGate = Station("Notting Hill Gate")

  val map1 = londonUnderground(
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

  @Test
  fun `can calculate number of changes`() {
    assertEquals(0, tufnellParkToHighgate.numChanges())
    assertEquals(1, highgateToOxfordCircus.numChanges())
    assertEquals(2, camdenToBondStreet.numChanges())
  }

  @Test
  fun `can calculate total duration`() {
    assertEquals(6, tufnellParkToHighgate.duration())
    assertEquals(18, highgateToOxfordCircus.duration())
    assertEquals(11, camdenToBondStreet.duration())
  }

  // added test
  @Test
  fun `sorts by duration time`() {
    assertEquals(
      """
                North Acton to Notting Hill Gate - 10 minutes, 0 changes
                 - North Acton to Notting Hill Gate by Central Line
                North Acton to Notting Hill Gate - 11 minutes, 0 changes
                 - North Acton to Notting Hill Gate by New Line
                
      """.trimIndent(),
      displayAllRoutes(map1.routesFrom(northActon, nottingHillGate))
    )
  }

  @Test
  fun `toString omits calling points`() {
    assertEquals(
      """
                Tufnell Park to Highgate - 6 minutes, 0 changes
                 - Tufnell Park to Highgate by Northern Line
      """.trimIndent(),
      tufnellParkToHighgate.toString()
    )
  }

  @Test
  fun `toString shows changes`() {
    assertEquals(
      """
                Highgate to Oxford Circus - 18 minutes, 1 changes
                 - Highgate to Euston by Northern Line
                 - Euston to Oxford Circus by Victoria Line
      """.trimIndent(),
      highgateToOxfordCircus.toString()
    )
  }
}
