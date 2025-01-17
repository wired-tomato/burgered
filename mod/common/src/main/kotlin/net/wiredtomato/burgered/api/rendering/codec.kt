package net.wiredtomato.burgered.api.rendering

import com.mojang.serialization.Codec
import net.minecraft.Util
import org.joml.Vector3d

val Vector3dCodec = Codec.DOUBLE.listOf().comapFlatMap(
    { list ->
        Util.fixedSize(list, 3).map { Vector3d(it[0], it[1], it[2]) }
    }, { vector ->
        listOf(vector.x, vector.y, vector.z)
    }
)
