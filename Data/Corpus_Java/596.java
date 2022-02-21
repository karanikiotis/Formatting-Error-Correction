/*
 * Copyright 1998-2014 University Corporation for Atmospheric Research/Unidata
 *
 *   Portions of this software were developed by the Unidata Program at the
 *   University Corporation for Atmospheric Research.
 *
 *   Access and use of this software shall impose the following obligations
 *   and understandings on the user. The user is granted the right, without
 *   any fee or cost, to use, copy, modify, alter, enhance and distribute
 *   this software, and any derivative works thereof, and its supporting
 *   documentation for any purpose whatsoever, provided that this entire
 *   notice appears in all copies of the software, derivative works and
 *   supporting documentation.  Further, UCAR requests that the user credit
 *   UCAR/Unidata in any publications that result from the use of this
 *   software or in any product that includes this software. The names UCAR
 *   and/or Unidata, however, may not be used in any advertising or publicity
 *   to endorse or promote any products or commercial entity unless specific
 *   written permission is obtained from UCAR/Unidata. The user also
 *   understands that UCAR/Unidata is not obligated to provide the user with
 *   any support, consulting, training or assistance of any kind with regard
 *   to the use, operation and performance of this software nor to provide
 *   the user with any updates, revisions, new versions or "bug fixes."
 *
 *   THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 *   INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 *   FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 *   NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 *   WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package ucar.unidata.geoloc;

import ucar.nc2.ft.PointFeature;

/**
 * A location on the earth, specified by lat, lon and optionally altitude.
 *
 * @author caron
 * @since Feb 18, 2008
 */
public interface EarthLocation {
  /**
   * Returns the latitude in some unit. The unit is very likely decimal degrees north, but we don't enforce that
   * anywhere.
   *
   * @return the latitude in some unit.
   */
  // FIXME: Enforce the "decimal degrees north" unit in EarthLocationImpl and other subclasses.
  // Or, allow a different unit and make it available from EarthLocation.
  double getLatitude();

  /**
   * Returns the longitude in some unit. The unit is very likely decimal degrees east, but we don't enforce that
   * anywhere.
   *
   * @return  the longitude in some unit.
   */
  // FIXME: Enforce the "decimal degrees east" unit in EarthLocationImpl and other subclasses.
  // Or, allow a different unit and make it available from EarthLocation.
  double getLongitude();

  /**
   * Returns the altitude in some unit. If this {@code EarthLocation} was retrieved from a {@link PointFeature}
   * (via {@link PointFeature#getLocation()}), then the unit can be obtained by calling
   * {@code pointFeature.getFeatureCollection().getAltUnits()}.
   *
   * @return  the altitude in some unit. A value of {@link Double#NaN} indicates "no altitude".
   */
  // FIXME: Make the unit available from EarthLocation.
  double getAltitude();

  /**
   * Get the lat/lon location
   * @return lat/lon location
   */
  LatLonPoint getLatLon();

  /**
   * Are either lat or lon missing?
   * @return true if lat or lon is missing
   */
  boolean isMissing();
}