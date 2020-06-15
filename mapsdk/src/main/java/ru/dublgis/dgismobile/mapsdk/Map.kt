package ru.dublgis.dgismobile.mapsdk

import ru.dublgis.dgismobile.mapsdk.clustering.Clusterer
import ru.dublgis.dgismobile.mapsdk.clustering.ClustererOptions
import ru.dublgis.dgismobile.mapsdk.geometries.carroute.CarRoute
import ru.dublgis.dgismobile.mapsdk.geometries.carroute.Segment
import ru.dublgis.dgismobile.mapsdk.geometries.circle.Circle
import ru.dublgis.dgismobile.mapsdk.geometries.circle.CircleOptions
import ru.dublgis.dgismobile.mapsdk.geometries.polygon.Polygon
import ru.dublgis.dgismobile.mapsdk.geometries.polygon.PolygonOptions
import ru.dublgis.dgismobile.mapsdk.geometries.polyline.Polyline
import ru.dublgis.dgismobile.mapsdk.geometries.polyline.PolylineOptions


typealias PositionChangeListener = (LonLat) -> Unit
typealias PropertyChangeListener = (Double) -> Unit
typealias PointerChangeListener = (MapPointerEvent) -> Unit


interface Map {

    var center: LonLat
    var rotation: Double

    var maxZoom: Double
    var minZoom: Double
    var zoom: Double

    var maxPitch: Double
    var minPitch: Double
    var pitch: Double

    fun addMarker(options: MarkerOptions): Marker

    fun removeMarker(marker: Marker)

    fun hideMarker(marker: Marker)

    fun showMarker(marker: Marker)

    fun setOnClickListener(listener: PointerChangeListener?)

    fun setOnCenterChangedListener(listener: PositionChangeListener?)

    fun setOnPitchChangedListener(listener: PropertyChangeListener?)

    fun setOnZoomChangedListener(listener: PropertyChangeListener?)

    fun setOnRotationChangedListener(listener: PropertyChangeListener?)

    fun setSelectedObjects(objects: Collection<MapObject>)

    fun createClusterer(options: ClustererOptions): Clusterer

    fun createPolyline(options: PolylineOptions): Polyline

    fun createPolygon(options: PolygonOptions): Polygon

    fun createCircle(options: CircleOptions): Circle

    fun createCarRoute(segments: Collection<Segment>): CarRoute
}