package it.unibo.lss.smart_parking.use_cases

import io.ktor.http.*
import it.unibo.lss.smart_parking.entity.Center
import it.unibo.lss.smart_parking.entity.ParkingSlot
import kotlinx.serialization.json.JsonObject
import java.time.Instant

/*
Copyright (c) 2022-2023 Antonio Iannotta & Luca Bracchi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
interface UseCases {
    fun occupySlot(userId: String, slotId: String, stopEnd: String, parkingSlotList: MutableList<ParkingSlot>): Pair<HttpStatusCode, JsonObject>
    fun incrementOccupation(userId: String, slotId: String, stopEnd: String, parkingSlotList: MutableList<ParkingSlot>): Pair<HttpStatusCode, JsonObject>
    fun freeSlot(slotId: String, parkingSlotList: MutableList<ParkingSlot>): Pair<HttpStatusCode, JsonObject>
    fun getAllParkingSlotsByRadius(center: Center): MutableList<ParkingSlot>
    fun getParkingSlotList(): MutableList<ParkingSlot>
    fun getParkingSlot(id: String): ParkingSlot

    fun isSlotOccupied(slotId: String, parkingSlotList: MutableList<ParkingSlot>): Boolean =
        parkingSlotList.first { ps -> ps.id == slotId
        }.occupied

    fun isTimeValid(actualTime: String, previousTime: String): Boolean {
        return Instant.parse(actualTime).isAfter(Instant.parse(previousTime))
    }

    fun isParkingSlotValid(slotId: String, parkingSlotList: MutableList<ParkingSlot>) =
        parkingSlotList.filter {
                parkingSlot -> parkingSlot.id == slotId
        }.isNotEmpty()

}